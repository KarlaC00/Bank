package com.banco.bank.mapper;

import com.banco.bank.model.Account;
import com.banco.bank.model.OperationType;
import com.banco.bank.model.Transactions;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperTransactions {
    public Transactions createTransactionOneAccount(Account account, Double amount, OperationType operationType) {
        return Transactions.builder()
                .account(account)
                .amount(amount)
                .operationType(operationType)
                .transactionDate(LocalDateTime.now())
                .build();
    }

    public Transactions createTransactionOTwoAccount(Account fromAccount , Account toAccount, Double amount, OperationType operationType) {
        return Transactions.builder()
                .account(fromAccount)
                .destinationAccount(toAccount)
                .amount(amount)
                .operationType(operationType)
                .transactionDate(LocalDateTime.now())
                .build();
    }
}