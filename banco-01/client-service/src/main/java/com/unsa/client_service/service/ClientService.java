package com.unsa.client_service.service;

import com.unsa.client_service.model.dto.ClientRequest;
import com.unsa.client_service.model.dto.ClientResponse;
import com.unsa.client_service.model.entity.ClientEntity;
import com.unsa.client_service.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;

    public void createClient(ClientRequest clientRequest){
        var clientEntity = ClientEntity.builder()
                .name(clientRequest.getName())
                .surname(clientRequest.getSurname())
                .address(clientRequest.getAddress())
                .contact(clientRequest.getContact())
                .phone(clientRequest.getPhone())
                .email(clientRequest.getEmail())
                .registerDate(clientRequest.getRegisterDate())
                .build();
        clientRepository.save(clientEntity);
        log.info("Created - Client: ");
    }

    public void deleteClient(String id){
        this.clientRepository.deleteById(id);
        log.info("Client deleted");
    }
    public List<ClientResponse> getAllClient(){
        var clients = clientRepository.findAll();
        return clients.stream().map(this::mapToClientResponse).toList();
    }

    public ClientResponse getClientById(String id){
        var client = clientRepository.findById(id);
        return client.map(this::mapToClientResponse).orElse(null);
    }
    private ClientResponse mapToClientResponse(ClientEntity clientEntity){
        return ClientResponse.builder()
                .id(clientEntity.getId())
                .name(clientEntity.getName())
                .surname(clientEntity.getSurname())
                .address(clientEntity.getAddress())
                .contact(clientEntity.getContact())
                .phone(clientEntity.getPhone())
                .email(clientEntity.getEmail())
                .registerDate(clientEntity.getRegisterDate())
                .build();
    }
}
