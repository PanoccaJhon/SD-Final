package com.unsa.notification_service.service;

import com.unsa.notification_service.event.TransactionEvent;
import com.unsa.notification_service.model.dto.NotificationRequest;
import com.unsa.notification_service.model.dto.NotificationResponse;
import com.unsa.notification_service.model.entity.NotificationEntity;
import com.unsa.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void createNotification(NotificationRequest notificationRequest){
        var notification = NotificationEntity.builder()
                .accountId(notificationRequest.getAccountId())
                .typeTransaction(notificationRequest.getTypeTransaction())
                .message(notificationRequest.getMessage())
                .build();
        this.notificationRepository.save(notification);
    }
    public List<NotificationResponse> finAll(){
        var notifications = notificationRepository.findAll();
        return notifications.stream().map(this::mapToNotificationResponse).toList();
    }

    public List<NotificationResponse> findAllByAccountId(String accountId){
        var notifications = notificationRepository.findAllByAccountId(accountId);
        return notifications.stream().map(this::mapToNotificationResponse).toList();
    }

    public NotificationResponse findByAccountId(String accountId){
        var notification = notificationRepository.findByAccountId(accountId);
        return mapToNotificationResponse(notification);
    }

    private NotificationResponse mapToNotificationResponse(NotificationEntity notificationEntity){
        return NotificationResponse.builder()
                .id(notificationEntity.getId())
                .accountId(notificationEntity.getAccountId())
                .typeTransaction(notificationEntity.getTypeTransaction())
                .message(notificationEntity.getMessage())
                .build();
    }
}
