package com.banco.bank.controller;

import com.banco.bank.dto.accountdto.AccountCreateDTO;
import com.banco.bank.model.Account;
import com.banco.bank.service.impl.ServiceAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final ServiceAccount serviceAccount;

    @PostMapping("/saving")
    public ResponseEntity<Account> createSavingAccount(@RequestBody AccountCreateDTO accountCreateDTO) {
        Account savingAccount = serviceAccount.createSavingAccount(accountCreateDTO);
        return ResponseEntity.ok().body(savingAccount);
    }

    @PostMapping("/checking")
    public ResponseEntity<Account> createCheckingAccount(@RequestBody AccountCreateDTO accountCreateDTO) {
        Account checkingAccount = serviceAccount.createCheckingAccount(accountCreateDTO);
        return ResponseEntity.ok().body(checkingAccount);
    }
}
