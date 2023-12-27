package com.unsa.account_service.model.dto;

import lombok.Data;

@Data
public class AccountDTO {
    private String userId;
    private String status;
    private String type;
    private double balance;
    private double maxCredit;
}
