package com.unsa.transaction_service.model.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private String id;
    private String number;
    private String clientId;
    private boolean status;
    private String type;
    private double balance;
    private double maxCredit;
}