package com.unsa.notification_service.service;

import com.unsa.notification_service.model.dto.NotificationRequest;
import com.unsa.notification_service.model.dto.NotificationResponse;
import com.unsa.notification_service.model.entity.NotificationEntity;
import com.unsa.notification_service.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private NotificationRepository notificationRepository;

    public void createNotification(NotificationRequest notificationRequest){
        var notification = NotificationEntity.builder()
                .accountId(notificationRequest.getAccountId())
                .destinyAccountId(notificationRequest.getDestinyAccountId())
                .title(notificationRequest.getTitle())
                .body(notificationRequest.getBody())
                .build();
        this.notificationRepository.save(notification);
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
                .destinyAccountId(notificationEntity.getAccountId())
                .title(notificationEntity.getTitle())
                .body(notificationEntity.getBody())
                .build();
    }
}
