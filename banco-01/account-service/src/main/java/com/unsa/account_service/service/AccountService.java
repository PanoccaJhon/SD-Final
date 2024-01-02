package com.unsa.account_service.service;

import com.unsa.account_service.model.dto.AccountRequest;
import com.unsa.account_service.model.dto.AccountResponse;
import com.unsa.account_service.model.entity.AccountEntity;
import com.unsa.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService{

    private final AccountRepository accountRepository;

    public void createAccount(AccountRequest accountRequest) {
        var accountEntity = AccountEntity.builder()
                .userId(accountRequest.getUserId())
                .status(accountRequest.getStatus())
                .type(accountRequest.getType())
                .balance(accountRequest.getBalance())
                .maxCredit(accountRequest.getMaxCredit())
                .build();
        accountRepository.save(accountEntity);
        log.info("Created - Account: ");
    }
    public AccountResponse getAccountById(String accountId) {
        var account =  accountRepository.findById(accountId);
        return account.map(this::mapToAccountResponse).orElse(null);
    }

    public List<AccountResponse> getAllAccountByUserId(String userId){
        var accounts = accountRepository.findAllByUserId(userId);
        return accounts.stream().map(this::mapToAccountResponse).toList();
    }

    public List<AccountResponse> getAllAccount(){
        var accounts = accountRepository.findAll();
        return accounts.stream().map(this::mapToAccountResponse).toList();
    }
    private AccountResponse mapToAccountResponse(AccountEntity account){
        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUserId())
                .status(account.getStatus())
                .type(account.getType())
                .balance(account.getBalance())
                .maxCredit(account.getMaxCredit())
                .build();
    }
}