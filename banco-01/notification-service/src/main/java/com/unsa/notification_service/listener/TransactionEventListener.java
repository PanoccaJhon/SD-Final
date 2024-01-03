package com.unsa.notification_service.listener;

import com.unsa.notification_service.event.TransactionEvent;
import com.unsa.notification_service.model.dto.NotificationRequest;
import com.unsa.notification_service.model.entity.NotificationEntity;
import com.unsa.notification_service.service.NotificationService;
import com.unsa.notification_service.utils.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class TransactionEventListener {
    private final NotificationService notificationService;

    @KafkaListener(topics = "transaction-events")
    public void handlerTransactionNotification(String message){
        var transactionEvent = JsonUtils.fromString(message, TransactionEvent.class);
        //Send notification
        var notification = NotificationRequest.builder()
                .accountId(transactionEvent.accountId())
                .typeTransaction(transactionEvent.typeTransaction())
                .message(transactionEvent.message())
                .build();
        this.notificationService.createNotification(notification);
        log.info("Received transaction event: {}",message);
    }
}
