package com.banco.bank.service.impl;

import com.banco.bank.dto.accountdto.*;
import com.banco.bank.mapper.MapperCheckingAccount;
import com.banco.bank.mapper.MapperSavingAccount;
import com.banco.bank.mapper.MapperTransactions;
import com.banco.bank.model.*;
import com.banco.bank.repository.RepositoryAccount;
import com.banco.bank.repository.RepositoryTransaction;
import com.banco.bank.service.IAccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class ServiceAccount implements IAccountService{

    private final MapperSavingAccount mapperSavingAccount;
    private final RepositoryAccount repositoryAccount;
    private final RepositoryTransaction repositoryTransaction;
    private final MapperCheckingAccount mapperCheckingAccount;
    private final Random random = new Random();
    private final MapperTransactions mapperTransactions;

    @Override
    public Account createCheckingAccount(AccountCreateDTO accountCreateDTO) {
        String uniqueAccountNumber;
        do {
            long randomDigits = (random.nextLong() & Long.MAX_VALUE) % 10_000_000_000L;
            uniqueAccountNumber = "33" + String.format("%010d", randomDigits);
        }while (repositoryAccount.existsAccountByAccountNumber(uniqueAccountNumber));

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
                .orElseThrow(() -> new EntityNotFoundException("Account not foundq"));

        account.setStatus(accountUpdateDTO.getStatus());
        account.setUpdatedAt(LocalDateTime.now());

        return repositoryAccount.save(account);
    }

    @Override
    public void deleteAccount(String accountNumber) {
        Account account = repositoryAccount.findByAccountNumber(accountNumber).orElseThrow(() -> new EntityNotFoundException("Account not found 1") );

        if(account.getBalance() == 0){
            repositoryAccount.delete(account);
        }else
            throw new IllegalArgumentException("You shouldn't have money in the account");
    }

    @Override
    public void withdraw(OperationAccountDTO operationAccountDTO) {
        Account account = repositoryAccount.findByAccountNumber(operationAccountDTO.getAccountNumber())
                .orElseThrow(() -> new EntityNotFoundException("Account not found1"));

        if ((account.getAccountType() == AccountType.SAVING || account.getAccountType() == AccountType.CHECKING)
                && account.getStatus() == StatusAccount.ACTIVE) {
            if (account.getBalance() >= operationAccountDTO.getAmount()) {
                account.setBalance(account.getBalance() - operationAccountDTO.getAmount());
                repositoryAccount.save(account);

                Transactions transaction = mapperTransactions.createTransactionOneAccount(
                        account,
                        operationAccountDTO.getAmount(),
                        OperationType.WITHDRAW
                );

                repositoryTransaction.save(transaction);
            } else {
                throw new IllegalArgumentException("Insufficient funds");
            }
        } else {
            throw new IllegalArgumentException("The account is not a type of account OR The account is not active");
        }
    }

    @Override
    public void deposit(OperationAccountDTO operationAccountDTO) {
        Account account = repositoryAccount.findByAccountNumber(operationAccountDTO.getAccountNumber())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        if ((account.getAccountType() == AccountType.SAVING || account.getAccountType() == AccountType.CHECKING)
                && account.getStatus() == StatusAccount.ACTIVE) {
            account.setBalance(account.getBalance() + operationAccountDTO.getAmount());
            repositoryAccount.save(account);

            Transactions transaction = mapperTransactions.createTransactionOneAccount(
                    account,
                    operationAccountDTO.getAmount(),
                    OperationType.DEPOSIT
            );

            repositoryTransaction.save(transaction);

        } else {
            throw new IllegalArgumentException("The account is not a type of account OR The account is not active");
        }
    }

    @Override
    public void transferMoney(TransferDTO transferDTO) {
        Account fromAccount = repositoryAccount.findByAccountNumber(transferDTO.getFromAccountNumber()).orElseThrow(() -> new RuntimeException("No found origin account"));
        Account toAccount = repositoryAccount.findByAccountNumber(transferDTO.getToAccountNumber()).orElseThrow(() -> new RuntimeException("No found origin account"));

        if(fromAccount.getStatus() == StatusAccount.ACTIVE && toAccount.getStatus() == StatusAccount.ACTIVE){
            if (fromAccount.getBalance() < transferDTO.getAmount()) {
                throw new IllegalArgumentException("Not enough money");
            }else{
                fromAccount.setBalance(fromAccount.getBalance() - transferDTO.getAmount());
                toAccount.setBalance(toAccount.getBalance() + transferDTO.getAmount());

                repositoryAccount.save(fromAccount);
                repositoryAccount.save(toAccount);

                Transactions transaction = mapperTransactions.createTransactionOTwoAccount(
                        fromAccount,
                        toAccount,
                        transferDTO.getAmount(),
                        OperationType.TRANSFER
                );

                repositoryTransaction.save(transaction);
            }
        }else{
            throw new IllegalArgumentException("The account is not active");
        }

    }

}
