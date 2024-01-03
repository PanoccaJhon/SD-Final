package com.unsa.notification_service.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "notifications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NotificationEntity {
    @Id
    private String id;
    private String accountId;
    private String typeTransaction;
    private String message;
}
