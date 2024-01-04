package com.unsa.transaction_service.service;

import com.unsa.transaction_service.model.dto.*;
import com.unsa.transaction_service.model.entity.TransactionEntity;
import com.unsa.transaction_service.repository.TransactionRepository;
import com.unsa.transaction_service.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public List<TransactionResponse> findAllTransaction(){
        var transactions = transactionRepository.findAll();
        return transactions.stream().map(this::mapTransactionEntityToTransactionResponse).toList();
    }

    public TransactionResponse findTransactionById(String id){
        var transaction = transactionRepository.findById(id);
        return transaction.map(this::mapTransactionEntityToTransactionResponse).orElse(null);
    }

    public TransactionResponse findTransactionByAccountId(String id){
        var transaction = transactionRepository.findTransactionEntitiesByAccountId();
        return mapTransactionEntityToTransactionResponse(transaction);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void createTransaction(TransactionRequest transactionRequest){
        var transaction = TransactionEntity.builder()
                .accountId(transactionRequest.getAccountId())
                .typeTransaction(transactionRequest.getTypeTransaction())
                .destinyAccountId(transactionRequest.getDestinyAccountId())
                .mountTransaction(transactionRequest.getMountTransaction())
                .statusTransaction(transactionRequest.isStatusTransaction())
                .build();
        if(transact(transactionRequest).state())
            transactionRepository.save(transaction);
        else
            log.info("No se puede completar la transaccion");
        throw new RuntimeException("Error en la transaccion en tiempo de ejecucion.");
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public TransactionResult transact(TransactionRequest transactionRequest){
        var type = transactionRequest.getTypeTransaction();
        return switch (type) {
            case "withdraw" -> this.withdraw(transactionRequest);
            case "deposit" -> this.deposit(transactionRequest);
            case "transfer" -> this.transfer(transactionRequest);
            default -> new TransactionResult(false, "Tipo de transaccion no permitida");
        };
    }

    private TransactionResult transfer(TransactionRequest transactionRequest) {
        var accountId = transactionRequest.getAccountId();
        var destinyAccountId = transactionRequest.getDestinyAccountId();
        var typeTransaction = transactionRequest.getTypeTransaction();
        String message;
        var stateWithdraw = this.withdraw(transactionRequest);
        if (stateWithdraw.state()) {
            transactionRequest.setAccountId(destinyAccountId);
            var stateDeposit = this.deposit(transactionRequest);
            if (stateDeposit.state()){
                var accountDestiny = this.webClientBuilder.build()
                        .get()
                        .uri("lb://account-service/api/account/{}",destinyAccountId)
                        .retrieve()
                        .bodyToMono(AccountResponse.class)
                        .block();
                message = "Se ha realizado una transferencia a la cuenta: "+accountDestiny.getNumber();
                this.sendKafkaEvent(new TransactionEvent(accountId, typeTransaction,message));
            }else
                throw new RuntimeException("Error al depositar, hacer RollBack.");
        }
        return new TransactionResult(false, "Fallo al retirar saldo");
    }

    private TransactionResult deposit(TransactionRequest transactionRequest) {
        var accountId = transactionRequest.getAccountId();
        var mount = transactionRequest.getMountTransaction();

        var transactionContent = new TransactionContent(accountId,mount);

        String uriDeposit = "lb://account-service/api/account/deposit";

        try{
            var transactionResult = this.webClientBuilder.build()
                    .post()
                    .uri(uriDeposit)
                    .body(transactionContent, TransactionContent.class)
                    .retrieve()
                    .bodyToMono(TransactionResult.class)
                    .block();
            assert transactionResult != null;
            if(transactionResult.state())
                this.sendKafkaEvent(new TransactionEvent(accountId, transactionRequest.getTypeTransaction(), transactionResult.message()));
            return transactionResult;
        }catch (Exception e){
            log.info("Internal error at deposit:{}",e.getMessage());
        }
        throw new RuntimeException();
    }

    private TransactionResult withdraw(TransactionRequest transactionRequest){
        var accountId = transactionRequest.getAccountId();
        var mount = transactionRequest.getMountTransaction();
        var typeTransaction = transactionRequest.getTypeTransaction();
        String message;
        var transactionContent = new TransactionContent(accountId,mount);
        String uriAvailable = "lb://account-service/api/account/available";
        String uriWithdraw = "lb://account-service/api/account/withdraw";
        try{
            var available = this.webClientBuilder.build()
                    .post()
                    .uri(uriAvailable)
                    .body(transactionContent, TransactionContent.class)
                    .retrieve()
                    .bodyToMono(TransactionResult.class)
                    .block();
            assert available != null;
            if(available.state()){
                var transactionResult = this.webClientBuilder.build()
                        .post()
                        .uri(uriWithdraw)
                        .retrieve()
                        .bodyToMono(TransactionResult.class)
                        .block();
                assert transactionResult != null;
                if(transactionResult.state()){
                    message = "Se ha realizado un retiro en su cuenta - Se recomienda revisar el estado de balance.\n"
                    +"Si esta seguro de la operacion ignore esto";
                    this.sendKafkaEvent(new TransactionEvent(accountId, typeTransaction,message));
                }
                return transactionResult;
            }else
                return available;
        }catch (NullPointerException e){
            log.info("Error: {}",e.getMessage());
            throw new RuntimeException();
        }
    }

    private void sendKafkaEvent(TransactionEvent transactionEvent){
        this.kafkaTemplate.send("transaction-events", JsonUtils.toJson(
                transactionEvent
        ));
    }

    private TransactionResponse mapTransactionEntityToTransactionResponse(TransactionEntity transactionEntity){
        return TransactionResponse.builder()
                .id(transactionEntity.getId())
                .accountId(transactionEntity.getAccountId())
                .typeTransaction(transactionEntity.getTypeTransaction())
                .destinyAccountId(transactionEntity.getDestinyAccountId())
                .mountTransaction(transactionEntity.getMountTransaction())
                .statusTransaction(transactionEntity.isStatusTransaction())
                .build();
    }

}
