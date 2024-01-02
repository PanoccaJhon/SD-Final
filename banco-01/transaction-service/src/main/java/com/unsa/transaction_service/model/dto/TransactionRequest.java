package com.unsa.transaction_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequest {
    private String clientId;
    private String accountId;
    private String typeTransaction;
    private String originAccountId;
    private String destinyAccountId;
    private double mountTransaction;
    private boolean statusTransaction;
}
