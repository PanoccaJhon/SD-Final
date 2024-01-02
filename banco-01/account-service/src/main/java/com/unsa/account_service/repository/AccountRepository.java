package com.unsa.account_service.repository;

import com.unsa.account_service.model.entity.AccountEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountRepository extends MongoRepository<AccountEntity, String> {
    List<AccountEntity> findAllByUserId(String userId);
}
