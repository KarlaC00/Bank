package com.banco.bank.service.impl;

import com.banco.bank.controller.AccountController;
import com.banco.bank.dto.accountdto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ControllerAccountTest {

    private MockMvc mockMvc;

    @Mock
    private ServiceAccount serviceAccount;

    @InjectMocks
    private AccountController accountController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void createSavingAccount_validInput_returnsSuccessMessage() throws Exception {
        AccountCreateDTO dto = new AccountCreateDTO("123456789", null, null);

        when(serviceAccount.createSavingAccount(any(AccountCreateDTO.class)))
                .thenReturn("Account created successfully");

        mockMvc.perform(post("/api/accounts/saving")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Account created successfully"));
    }

    @Test
    void deleteAccount_validAccountNumber_returnsNoContent() throws Exception {
        String accountNumber = "3312345678";

        doNothing().when(serviceAccount).deleteAccount(accountNumber);

        mockMvc.perform(delete("/api/accounts/by-accountNumber/{accountNumber}", accountNumber))
                .andExpect(status().isNoContent());
    }

    @Test
    void withdraw_validInput_returnsOk() throws Exception {
        OperationAccountDTO dto = new OperationAccountDTO("3312345678", 50.0);

        doNothing().when(serviceAccount).withdraw(any(OperationAccountDTO.class));

        mockMvc.perform(post("/api/accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deposit_validInput_returnsOk() throws Exception {
        OperationAccountDTO dto = new OperationAccountDTO("5312345678", 100.0);

        doNothing().when(serviceAccount).deposit(any(OperationAccountDTO.class));

        mockMvc.perform(post("/api/accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void transferMoney_validInput_returnsOk() throws Exception {
        TransferDTO dto = new TransferDTO("3312345678", "5312345678", 75.0);

        doNothing().when(serviceAccount).transferMoney(any(TransferDTO.class));

        mockMvc.perform(post("/api/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
