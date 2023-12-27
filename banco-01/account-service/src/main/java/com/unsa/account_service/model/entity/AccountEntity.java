package com.unsa.account_service.model.entity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "accounts")
@Builder
public class AccountEntity {
    @Id
    private String id;
    private String userId;
    private String status;
    private String type;
    private double balance;
    private double maxCredit;
}
