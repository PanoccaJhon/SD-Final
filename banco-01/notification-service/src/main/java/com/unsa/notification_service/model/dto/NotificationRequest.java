package com.unsa.notification_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private String accountId;
    private String destinyAccountId;
    private String title;
    private String body;
}
