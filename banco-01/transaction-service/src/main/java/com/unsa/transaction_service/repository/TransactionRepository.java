package com.unsa.transaction_service.repository;

import com.unsa.transaction_service.model.dto.TransactionResponse;
import com.unsa.transaction_service.model.entity.TransactionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<TransactionEntity,String> {
    public TransactionEntity findTransactionEntitiesByAccountId();
}
