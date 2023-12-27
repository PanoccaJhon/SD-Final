package com.unsa.account_service.controller;


import com.unsa.account_service.model.dto.AccountRequest;
import com.unsa.account_service.model.dto.AccountResponse;
import com.unsa.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
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

    // Otros métodos del controlador según las necesidades
}