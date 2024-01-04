package com.unsa.account_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequest {
    private String number;
    private String clientId;
    private boolean status;
    private String type;
    private double balance;
    private double maxCredit;
}