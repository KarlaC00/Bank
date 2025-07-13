package com.banco.bank.service.impl;


import com.banco.bank.dto.accountdto.*;
import com.banco.bank.mapper.MapperCheckingAccount;
import com.banco.bank.mapper.MapperSavingAccount;
import com.banco.bank.model.*;
import com.banco.bank.repository.RepositoryAccount;
import com.banco.bank.service.IAccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class ServiceAccount implements IAccountService {

    private final MapperSavingAccount mapperSavingAccount;
    private final RepositoryAccount repositoryAccount;
    private final MapperCheckingAccount mapperCheckingAccount;
    private final Random random = new Random();

    @Override
    public Account createCheckingAccount(AccountCreateDTO accountCreateDTO) {
        String uniqueAccountNumber;
        do {
            long randomDigits = (random.nextLong() & Long.MAX_VALUE) % 10_000_000_000L;
            uniqueAccountNumber = "33" + String.format("%010d", randomDigits);
        } while (repositoryAccount.existsAccountByAccountNumber(uniqueAccountNumber));

        accountCreateDTO.setAccountNumber(uniqueAccountNumber);

        Account account = mapperCheckingAccount.createCheckingAccount(accountCreateDTO);
        return repositoryAccount.save(account);
    }


    @Override
    public Account createSavingAccount(AccountCreateDTO accountCreateDTO) {
        String uniqueAccountNumber;
        do {
            long randomDigits = (random.nextLong() & Long.MAX_VALUE) % 10_000_000_000L;
            uniqueAccountNumber = "53" + String.format("%010d", randomDigits);
        } while (repositoryAccount.existsAccountByAccountNumber(uniqueAccountNumber));

        accountCreateDTO.setAccountNumber(uniqueAccountNumber);

        Account account = mapperSavingAccount.createSavingAccount(accountCreateDTO);

        return repositoryAccount.save(account);
    }

    @Override
    public Account updateAccount(String accountNumber, AccountUpdateDTO accountUpdateDTO) {
        Account account = repositoryAccount.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        account.setStatus(accountUpdateDTO.getStatus());
        account.setUpdatedAt(LocalDateTime.now());

        return repositoryAccount.save(account);
    }
}