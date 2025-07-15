package com.banco.bank.controller;

import com.banco.bank.dto.accountdto.AccountCreateDTO;
import com.banco.bank.dto.accountdto.AccountUpdateDTO;
import com.banco.bank.dto.accountdto.OperationAccountDTO;
import com.banco.bank.dto.accountdto.TransferDTO;
import com.banco.bank.model.Account;
import com.banco.bank.service.impl.ServiceAccount;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final ServiceAccount serviceAccount;

    @PostMapping("/saving")
    public String createSavingAccount(@RequestBody @Valid AccountCreateDTO accountCreateDTO) {
        return serviceAccount.createSavingAccount(accountCreateDTO);
    }

    @PostMapping("/checking")
    public String createCheckingAccount(@RequestBody @Valid AccountCreateDTO accountCreateDTO) {
        return serviceAccount.createCheckingAccount(accountCreateDTO);
    }

    @PutMapping("/by-accountNumber/{accountNumber}")
    public ResponseEntity<Account> updateAccount(@PathVariable String accountNumber, @RequestBody AccountUpdateDTO accountUpdateDTO) {
        Account updatedAccount = serviceAccount.updateAccount(accountNumber, accountUpdateDTO);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/by-accountNumber/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        serviceAccount.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody @Valid OperationAccountDTO operationAccountDTO) {
        serviceAccount.withdraw(operationAccountDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestBody @Valid OperationAccountDTO operationAccountDTO) {
        serviceAccount.deposit(operationAccountDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transferMoney(@RequestBody @Valid TransferDTO transferDTO) {
        serviceAccount.transferMoney(transferDTO);
        return ResponseEntity.ok().build();
    }
}
