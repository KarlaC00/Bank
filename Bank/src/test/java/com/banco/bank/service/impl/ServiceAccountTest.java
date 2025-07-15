package com.banco.bank.service.impl;

import com.banco.bank.dto.accountdto.AccountCreateDTO;
import com.banco.bank.dto.accountdto.OperationAccountDTO;
import com.banco.bank.dto.accountdto.TransferDTO;
import com.banco.bank.mapper.MapperCheckingAccount;
import com.banco.bank.mapper.MapperSavingAccount;
import com.banco.bank.mapper.MapperTransactions;
import com.banco.bank.model.Account;
import com.banco.bank.model.AccountType;
import com.banco.bank.model.ExemptGMF;
import com.banco.bank.model.StatusAccount;
import com.banco.bank.model.Transactions;
import com.banco.bank.repository.RepositoryAccount;
import com.banco.bank.repository.RepositoryTransaction;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServiceAccountTest {

    @Mock private MapperSavingAccount mapperSavingAccount;
    @Mock private RepositoryAccount repositoryAccount;
    @Mock private RepositoryTransaction repositoryTransaction;
    @Mock private MapperCheckingAccount mapperCheckingAccount;
    @Mock private MapperTransactions mapperTransactions;

    private ServiceAccount serviceAccount;
    private Validator validator;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        serviceAccount = new ServiceAccount(
                validator,
                mapperSavingAccount,
                repositoryAccount,
                repositoryTransaction,
                mapperCheckingAccount,
                mapperTransactions
        );
    }


    @Test
    void createCheckingAccount_valid_generatesValidAccountNumber() {
        AccountCreateDTO dto = new AccountCreateDTO("123456789", null, ExemptGMF.NO_EXEMPT);
        when(repositoryAccount.existsAccountByAccountNumber(anyString())).thenReturn(false);
        when(mapperCheckingAccount.createCheckingAccount(any())).thenReturn(new Account());

        String result = serviceAccount.createCheckingAccount(dto);

        assertEquals("Account created successfully", result);
        verify(repositoryAccount).save(any(Account.class));
        assertNotNull(dto.getAccountNumber());
        assertTrue(dto.getAccountNumber().startsWith("33"));
        assertEquals(12, dto.getAccountNumber().length());
    }

    @Test
    void createSavingAccount_valid_generatesValidAccountNumber() {
        AccountCreateDTO dto = new AccountCreateDTO("987654321", null, ExemptGMF.EXEMPT);
        when(repositoryAccount.existsAccountByAccountNumber(anyString())).thenReturn(false);
        when(mapperSavingAccount.createSavingAccount(any())).thenReturn(new Account());

        String result = serviceAccount.createSavingAccount(dto);

        assertEquals("Account created successfully", result);
        verify(repositoryAccount).save(any(Account.class));
        assertTrue(dto.getAccountNumber().startsWith("53"));
        assertEquals(12, dto.getAccountNumber().length());
    }

    // ❌ TESTS INVÁLIDOS

    @Test
    void createCheckingAccount_invalid_shouldThrowValidationError() {
        AccountCreateDTO dto = new AccountCreateDTO("", null, null); // identification vacía

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> serviceAccount.createCheckingAccount(dto));

        assertFalse(exception.getConstraintViolations().isEmpty());
    }

    @Test
    void createSavingAccount_invalid_shouldThrowValidationError() {
        AccountCreateDTO dto = new AccountCreateDTO("ABC", null, null); // identification no numérica

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> serviceAccount.createSavingAccount(dto));

        assertFalse(exception.getConstraintViolations().isEmpty());
    }


    @Test
    void deleteAccount_valid() {
        String accNumber = "331234567890";
        Account account = new Account();
        account.setBalance(0.0);
        account.setStatus(StatusAccount.ACTIVE);

        when(repositoryAccount.findByAccountNumber(accNumber)).thenReturn(Optional.of(account));

        serviceAccount.deleteAccount(accNumber);

        assertEquals(StatusAccount.CANCELLED, account.getStatus());
        verify(repositoryAccount).save(account);
    }

    @Test
    void deleteAccount_invalid_balanceNotZero() {
        String accNumber = "531234567890";
        Account account = new Account();
        account.setBalance(100.0);

        when(repositoryAccount.findByAccountNumber(accNumber)).thenReturn(Optional.of(account));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> serviceAccount.deleteAccount(accNumber));
        assertEquals("You shouldn't have money in the account", ex.getMessage());
    }

    @Test
    void withdraw_valid() {
        OperationAccountDTO dto = new OperationAccountDTO("3312345678", 50.0);
        Account account = new Account();
        account.setBalance(100.0);
        account.setStatus(StatusAccount.ACTIVE);
        account.setAccountType(AccountType.CHECKING);

        when(repositoryAccount.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));
        when(mapperTransactions.createTransactionOneAccount(any(), any(), any())).thenReturn(new Transactions());

        serviceAccount.withdraw(dto);

        assertEquals(50.0, account.getBalance());
        verify(repositoryTransaction).save(any());
    }

    @Test
    void withdraw_invalid_insufficientFunds() {
        OperationAccountDTO dto = new OperationAccountDTO("3312345678", 200.0);
        Account account = new Account();
        account.setBalance(100.0);
        account.setStatus(StatusAccount.ACTIVE);
        account.setAccountType(AccountType.SAVING);

        when(repositoryAccount.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> serviceAccount.withdraw(dto));
        assertEquals("Insufficient funds", ex.getMessage());
    }

    @Test
    void deposit_valid() {
        OperationAccountDTO dto = new OperationAccountDTO("5312345678", 200.0);
        Account account = new Account();
        account.setBalance(100.0);
        account.setStatus(StatusAccount.ACTIVE);
        account.setAccountType(AccountType.SAVING);

        when(repositoryAccount.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));
        when(mapperTransactions.createTransactionOneAccount(any(), any(), any())).thenReturn(new Transactions());

        serviceAccount.deposit(dto);

        assertEquals(300.0, account.getBalance());
        verify(repositoryTransaction).save(any());
    }

    @Test
    void deposit_invalid_inactiveAccount() {
        OperationAccountDTO dto = new OperationAccountDTO("5312345678", 100.0);
        Account account = new Account();
        account.setBalance(50.0);
        account.setStatus(StatusAccount.CANCELLED);
        account.setAccountType(AccountType.SAVING);

        when(repositoryAccount.findByAccountNumber(dto.getAccountNumber())).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> serviceAccount.deposit(dto));
    }

    @Test
    void transferMoney_valid() {
        TransferDTO dto = new TransferDTO("3312345678", "5312345678", 50.0);
        Account from = new Account();
        from.setBalance(100.0);
        from.setStatus(StatusAccount.ACTIVE);
        Account to = new Account();
        to.setBalance(200.0);
        to.setStatus(StatusAccount.ACTIVE);

        when(repositoryAccount.findByAccountNumber(dto.getFromAccountNumber())).thenReturn(Optional.of(from));
        when(repositoryAccount.findByAccountNumber(dto.getToAccountNumber())).thenReturn(Optional.of(to));
        when(mapperTransactions.createTransactionOTwoAccount(any(), any(), any(), any())).thenReturn(new Transactions());

        serviceAccount.transferMoney(dto);

        assertEquals(50.0, from.getBalance());
        assertEquals(250.0, to.getBalance());
        verify(repositoryTransaction).save(any());
    }

    @Test
    void transferMoney_invalid_insufficientFunds() {
        TransferDTO dto = new TransferDTO("3312345678", "5312345678", 200.0);
        Account from = new Account();
        from.setBalance(100.0);
        from.setStatus(StatusAccount.ACTIVE);
        Account to = new Account();
        to.setBalance(300.0);
        to.setStatus(StatusAccount.ACTIVE);

        when(repositoryAccount.findByAccountNumber(dto.getFromAccountNumber())).thenReturn(Optional.of(from));
        when(repositoryAccount.findByAccountNumber(dto.getToAccountNumber())).thenReturn(Optional.of(to));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> serviceAccount.transferMoney(dto));
        assertEquals("Not enough money", ex.getMessage());
    }
}

