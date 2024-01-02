package com.unsa.transaction_service.model.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "transaction")
public class TransactionEntity {
    public String id;

}
