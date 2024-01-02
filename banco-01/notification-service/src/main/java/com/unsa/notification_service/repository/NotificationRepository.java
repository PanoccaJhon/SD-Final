package com.unsa.notification_service.repository;

import com.unsa.notification_service.model.entity.NotificationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<NotificationEntity, String> {
    List<NotificationEntity> findAllByAccountId(String accountId);
    NotificationEntity findByAccountId(String accountId);
}
