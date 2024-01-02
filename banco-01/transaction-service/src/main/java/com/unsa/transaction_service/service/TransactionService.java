package com.unsa.transaction_service.service;

import com.unsa.transaction_service.model.dto.TransactionRequest;
import com.unsa.transaction_service.model.dto.TransactionResponse;
import com.unsa.transaction_service.model.entity.TransactionEntity;
import com.unsa.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;

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
        transactionRepository.save(transaction);
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

}
