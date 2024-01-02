package com.unsa.client_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientResponse {
    private String id;
    private String name;
    private String surname;
    private String address;
    private String contact;
    private String phone;
    private String email;
    private String registerDate;
}
