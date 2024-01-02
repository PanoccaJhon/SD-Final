package com.unsa.notification_service.controller;

import com.unsa.notification_service.model.dto.NotificationRequest;
import com.unsa.notification_service.model.dto.NotificationResponse;
import com.unsa.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNotification(@RequestBody NotificationRequest notificationRequest){
        this.notificationService.createNotification(notificationRequest);
    }

    @GetMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationResponse getNotificationByAccountId(@PathVariable String accountId){
        return this.notificationService.findByAccountId(accountId);
    }

    @GetMapping("/{accountId}/all")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationResponse> getAllNotificationByAccountId(@PathVariable String accountId){
        return this.notificationService.findAllByAccountId(accountId);
    }
}
