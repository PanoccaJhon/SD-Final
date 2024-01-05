package com.unsa.account_service.service;

import com.unsa.account_service.model.dto.AccountRequest;
import com.unsa.account_service.model.dto.AccountResponse;
import com.unsa.account_service.model.dto.TransactionContent;
import com.unsa.account_service.model.dto.TransactionResult;
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

    public TransactionResult availableBalance(TransactionContent transactionContent){
        var account = this.accountRepository.findById(transactionContent.accountId());
        TransactionResult transactionResult;
        if( account.isEmpty())
            return new TransactionResult(false,"No existe la cuenta");
        double balance = account.get().getBalance();
        double mount = transactionContent.mount();
        if (balance < mount)
            return new TransactionResult(false, "Saldo insuficiente");
        else
            return new TransactionResult(true, "Saldo disponible");
    }
    public TransactionResult deposit(TransactionContent transactionContent){
        var account = this.accountRepository.findById(transactionContent.accountId());
        if(account.isEmpty())
            return new TransactionResult(false, "No existe la cuenta");
        var newBalance = account.get().getBalance() + transactionContent.mount();
        account.get().setBalance(newBalance);
        this.accountRepository.save(account.get());
        return new TransactionResult(true, "Se he realizado el deposito con exito");
    }
    public TransactionResult withdraw(TransactionContent transactionContent){
        var available = this.availableBalance(transactionContent);
        if (available.state())
            return available;

        var account = this.accountRepository.findById(transactionContent.accountId());
        var newBalance = account.get().getBalance() - transactionContent.mount();
        account.get().setBalance(newBalance);
        this.accountRepository.save(account.get());
        return new TransactionResult(true, "Se ha retirado con exito");
    }

    public void createAccount(AccountRequest accountRequest) {
        var accountEntity = AccountEntity.builder()
                .number(accountRequest.getNumber())
                .clientId(accountRequest.getClientId())
                .status(accountRequest.isStatus())
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
    public List<AccountResponse> getAllAccountByClientId(String userId){
        var accounts = accountRepository.findAllByClientId(userId);
        return accounts.stream().map(this::mapToAccountResponse).toList();
    }
    public List<AccountResponse> getAllAccount(){
        var accounts = accountRepository.findAll();
        return accounts.stream().map(this::mapToAccountResponse).toList();
    }
    private AccountResponse mapToAccountResponse(AccountEntity account){
        return AccountResponse.builder()
                .id(account.getId())
                .number(account.getNumber())
                .clientId(account.getClientId())
                .status(account.isStatus())
                .type(account.getType())
                .balance(account.getBalance())
                .maxCredit(account.getMaxCredit())
                .build();
    }
}