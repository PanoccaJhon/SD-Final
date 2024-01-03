package com.unsa.transaction_service.controller;

import com.unsa.transaction_service.model.dto.TransactionRequest;
import com.unsa.transaction_service.model.dto.TransactionResponse;
import com.unsa.transaction_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody TransactionRequest transactionRequest){
        this.transactionService.createTransaction(transactionRequest);
    }

    @GetMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse getTransactionById(@PathVariable String transactionId){
        return this.transactionService.findTransactionById(transactionId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponse> getAll(){
        return this.transactionService.findAllTransaction();
    }
}
