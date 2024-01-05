package com.unsa.account_service.controller;


import com.unsa.account_service.model.dto.AccountRequest;
import com.unsa.account_service.model.dto.AccountResponse;
import com.unsa.account_service.model.dto.TransactionContent;
import com.unsa.account_service.model.dto.TransactionResult;
import com.unsa.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@RequestBody AccountRequest accountRequest) {
        this.accountService.createAccount(accountRequest);
    }

    @GetMapping("/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse getAccountById(@PathVariable String accountId) {
        return this.accountService.getAccountById(accountId);
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResult isAvailable(@RequestBody TransactionContent transactionContent){
        return this.accountService.availableBalance(transactionContent);
    }
    @GetMapping("/deposit")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResult deposit(@RequestBody TransactionContent transactionContent){
        return this.accountService.deposit(transactionContent);
    }
    @GetMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResult withdraw(@RequestBody TransactionContent transactionContent){
        return this.accountService.withdraw(transactionContent);
    }

    @GetMapping("/{clientId}/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponse> getAllAccountByClientId(@PathVariable String clientId){
        return this.accountService.getAllAccountByClientId(clientId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponse> getAllAccounts(){
        return this.accountService.getAllAccount();
    }

}