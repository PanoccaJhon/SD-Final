package com.unsa.notification_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    @Id
    private String id;
    private String accountId;
    private String destinyAccountId;
    private String title;
    private String body;
}
