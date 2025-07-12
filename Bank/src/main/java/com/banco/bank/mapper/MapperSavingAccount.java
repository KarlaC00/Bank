package com.banco.bank.mapper;

import com.banco.bank.dto.accountdto.AccountCreateDTO;
import com.banco.bank.model.Account;
import com.banco.bank.model.AccountType;
import com.banco.bank.model.StatusAccount;
import com.banco.bank.service.impl.ServiceClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperSavingAccount {

    private final ServiceClient serviceClient;

    public MapperSavingAccount(ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    public Account createSavingAccount(AccountCreateDTO accountCreateDTO) {
        return Account.builder()
                .accountNumber(accountCreateDTO.getAccountNumber())
                .accountType(AccountType.SAVING)
                .client(serviceClient.getClientByIdentificationNumber(accountCreateDTO.getIdentificationNumber()))
                .exemptGMF(accountCreateDTO.getExemptGMF())
                .status(StatusAccount.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
    }
}