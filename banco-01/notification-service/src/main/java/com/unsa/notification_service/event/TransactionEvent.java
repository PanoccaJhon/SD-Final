package com.unsa.notification_service.event;

public record TransactionEvent(String accountId, String typeTransaction, String message) {
}
