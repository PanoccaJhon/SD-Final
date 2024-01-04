package com.unsa.transaction_service.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TransactionEntity {
    @Id
    private String id;
    private String accountId;
    private String typeTransaction;
    private String destinyAccountId;
    private double mountTransaction;
    private boolean statusTransaction;
}
