package com.unsa.transaction_service.event;

public record TransactionEvent(String accountId, String typeTransaction, String message ) {
}
