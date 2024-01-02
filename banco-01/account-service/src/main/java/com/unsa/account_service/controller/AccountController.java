package com.unsa.account_service.controller;


import com.unsa.account_service.model.dto.AccountRequest;
import com.unsa.account_service.model.dto.AccountResponse;
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

    @GetMapping("/{userId}/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponse> getAllAccountByUserId(@PathVariable String userId){
        return this.accountService.getAllAccountByUserId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponse> getAllAccounts(){
        return this.accountService.getAllAccount();
    }

    // Otros métodos del controlador según las necesidades
}