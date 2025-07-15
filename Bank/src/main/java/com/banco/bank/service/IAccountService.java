package com.banco.bank.service;

import com.banco.bank.dto.accountdto.*;
import com.banco.bank.model.Account;

public interface IAccountService {
    String createCheckingAccount(AccountCreateDTO accountCreateDTO);
    String createSavingAccount(AccountCreateDTO accountCreateDTO);
    Account updateAccount(String accountNumber, AccountUpdateDTO accountUpdateDTO);
    void deleteAccount(String accountNumber);
    void withdraw(OperationAccountDTO operationAccountDTO);
    void deposit(OperationAccountDTO operationAccountDTO);
    void transferMoney(TransferDTO transferDTO);
}