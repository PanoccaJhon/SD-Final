package com.unsa.account_service.service;

import com.unsa.account_service.model.dto.AccountDTO;
import com.unsa.account_service.model.dto.AccountRequest;
import com.unsa.account_service.model.dto.AccountResponse;
import com.unsa.account_service.model.entity.AccountEntity;
import com.unsa.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    // Otros métodos relacionados con la lógica de negocio
}