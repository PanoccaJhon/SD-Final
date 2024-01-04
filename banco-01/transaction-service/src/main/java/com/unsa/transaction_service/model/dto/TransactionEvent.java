package com.unsa.transaction_service.model.dto;

public record TransactionEvent(String accountId, String typeTransaction, String message ) {
}
