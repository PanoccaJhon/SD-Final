package com.unsa.transaction_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private String clientId;
    private String accountId;
    private String typeTransaction;
    private String originAccountId;
    private String destinyAccountId;
    private double mountTransaction;
    private boolean statusTransaction;
}
