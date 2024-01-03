package com.unsa.transaction_service.service;

import com.unsa.transaction_service.event.TransactionEvent;
import com.unsa.transaction_service.model.dto.TransactionRequest;
import com.unsa.transaction_service.model.dto.TransactionResponse;
import com.unsa.transaction_service.model.entity.TransactionEntity;
import com.unsa.transaction_service.repository.TransactionRepository;
import com.unsa.transaction_service.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
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

    @Transactional
    public void createTransaction(TransactionRequest transactionRequest){
        var transaction = TransactionEntity.builder()
                .clientId(transactionRequest.getClientId())
                .accountId(transactionRequest.getAccountId())
                .typeTransaction(transactionRequest.getTypeTransaction())
                .originAccountId(transactionRequest.getOriginAccountId())
                .destinyAccountId(transactionRequest.getDestinyAccountId())
                .mountTransaction(transactionRequest.getMountTransaction())
                .statusTransaction(transactionRequest.isStatusTransaction())
                .build();
        if(transact(transactionRequest))
            transactionRepository.save(transaction);
        else
            log.info("No se puede completar la transaccion");
    }

    public List<TransactionResponse> findAllTransaction(){
        var transactions = transactionRepository.findAll();
        return transactions.stream().map(this::mapToTransactionResponse).toList();
    }

    public TransactionResponse findTransactionById(String id){
        var transaction = transactionRepository.findById(id);
        return transaction.map(this::mapToTransactionResponse).orElse(null);
    }

    public TransactionResponse findTransactionByAccountId(String id){
        var transaction = transactionRepository.findTransactionEntitiesByAccountId();
        return mapToTransactionResponse(transaction);
    }

    private boolean updateAccounts(TransactionRequest transactionRequest){
        return false;
    }

    private TransactionResponse mapToTransactionResponse(TransactionEntity transactionEntity){
        return TransactionResponse.builder()
                .id(transactionEntity.getId())
                .clientId(transactionEntity.getClientId())
                .accountId(transactionEntity.getAccountId())
                .typeTransaction(transactionEntity.getTypeTransaction())
                .originAccountId(transactionEntity.getOriginAccountId())
                .destinyAccountId(transactionEntity.getDestinyAccountId())
                .mountTransaction(transactionEntity.getMountTransaction())
                .statusTransaction(transactionEntity.isStatusTransaction())
                .build();
    }

    private boolean transact(TransactionRequest transactionRequest){
        var type = transactionRequest.getTypeTransaction();
        return switch (type) {
            case "withdraw" -> this.withdraw(transactionRequest);
            case "deposit" -> this.deposit(transactionRequest);
            case "transfer" -> this.transfer(transactionRequest);
            default -> false;
        };
    }

    private boolean transfer(TransactionRequest transactionRequest) {
        var accountId = transactionRequest.getAccountId();
        var destinyAccountId = transactionRequest.getDestinyAccountId();
        var typeTransaction = transactionRequest.getTypeTransaction();
        var mount = transactionRequest.getMountTransaction();
        String message;
        boolean okWithdraw = this.withdraw(transactionRequest);
        if (okWithdraw) {
            boolean okDeposit = this.deposit(transactionRequest);
            if (okDeposit){
                message = "Se ha realizado una transferencia";
                this.sendKafkaEvent(new TransactionEvent(accountId, typeTransaction,message));
            }
        }
        return okWithdraw;
    }

    private boolean deposit(TransactionRequest transactionRequest) {
        var accountId = transactionRequest.getAccountId();
        var mount = transactionRequest.getMountTransaction();
        String message;
        try{
            var ok =  this.webClientBuilder.build()
                    .get()
                    .uri("lb://account-service/api/account/{accountId}/deposit/{mount}",accountId,mount)
                    .retrieve()
                    .bodyToMono(Boolean.class).block();
            if(ok) {
                message = "Se ha realizado un deposito en su cuenta - Revice su estado de cuenta";
                this.sendKafkaEvent(new TransactionEvent(accountId, transactionRequest.getTypeTransaction(), message));
            }
            return Boolean.TRUE.equals(ok);
        }catch (NullPointerException e){
            log.info("Error at deposit:{}",e.getMessage());
            return false;
        }
    }

    private boolean withdraw(TransactionRequest transactionRequest){
        var accountId = transactionRequest.getAccountId();
        var mount = transactionRequest.getMountTransaction();
        var typeTransaction = transactionRequest.getTypeTransaction();
        String message;
        try{
            var available = this.webClientBuilder.build()
                    .get()
                    .uri("lb://account-service/api/account/{accountId}/available/{mount}",accountId,mount)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            if(available){
                var ok = this.webClientBuilder.build()
                        .get()
                        .uri("lb://account-service/api/account/{accountId}/withdraw/{mount}",accountId,mount)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .block();
                if(ok){
                    message = "Se ha realizado un retiro en su cuenta - Se recomienda revisar el estado de balance.\n"
                    +"Si esta seguro de la operacion ignore esto";
                    this.sendKafkaEvent(new TransactionEvent(accountId, typeTransaction,message));
                }
                return Boolean.TRUE.equals(ok);
            }else
                return false;
        }catch (NullPointerException e){
            log.info("Error: {}",e.getMessage());
            return false;
        }
    }

    private void sendKafkaEvent(TransactionEvent transactionEvent){
        this.kafkaTemplate.send("transaction-events", JsonUtils.toJson(
                transactionEvent
        ));
    }
}
