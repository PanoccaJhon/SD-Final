package com.unsa.client_service.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "clients")
@Builder
public class ClientEntity {

    @Id
    private String id;
    private String name;
    private String surname;
    private String address;
    private String contact;
    private String phone;
    private String email;
    private String registerDate;
}
