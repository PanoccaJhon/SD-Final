package com.unsa.client_service.controller;

import com.unsa.client_service.model.dto.ClientRequest;
import com.unsa.client_service.model.dto.ClientResponse;
import com.unsa.client_service.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody ClientRequest clientRequest){
        this.clientService.createClient(clientRequest);
    }

    @GetMapping("/{clientId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteClientById(@PathVariable String clientId){
        this.clientService.deleteClient(clientId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientResponse> getAllClients(){
        return this.clientService.getAllClient();
    }

    @GetMapping("/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ClientResponse getClientById(@PathVariable String clientId){
        return clientService.getClientById(clientId);
    }


}
