package com.banco.bank.service.impl;

import com.banco.bank.controller.ClientController;
import com.banco.bank.dto.clientdto.ClientCreateDTO;
import com.banco.bank.dto.clientdto.UpdateClientDTO;
import com.banco.bank.model.Client;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ControllerClientTest {

    private MockMvc mockMvc;

    @Mock
    private ServiceClient clientService;

    @InjectMocks
    private ClientController clientController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    @Test
    void getClientById_ShouldReturnClient() throws Exception {
        Client client = new Client();
        client.setIdentificationNumber("12345");
        client.setNames("Carlos");
        client.setLastName("Ruiz");
        client.setEmail("carlos@example.com");
        client.setBirthDate(LocalDate.of(1980, 1, 1));

        when(clientService.getClientByIdentificationNumber("12345"))
                .thenReturn(client);


        mockMvc.perform(get("/api/clients/by-identification/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names").value("Carlos"))
                .andExpect(jsonPath("$.email").value("carlos@example.com"));
    }

    @Test
    void updateClient_ShouldReturnUpdatedClient() throws Exception {
        UpdateClientDTO updateDTO = new UpdateClientDTO();
        updateDTO.setNames("Laura");
        updateDTO.setLastName("Ramírez");
        updateDTO.setEmail("laura@new.com");

        Client updated = new Client();
        updated.setIdentificationNumber("54321");
        updated.setNames("Laura");
        updated.setLastName("Ramírez");
        updated.setEmail("laura@new.com");
        updated.setBirthDate(LocalDate.of(1990, 3, 3));

        when(clientService.updateClient(eq("54321"), any(UpdateClientDTO.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/clients/by-identification/54321")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("laura@new.com"))
                .andExpect(jsonPath("$.lastName").value("Ramírez"));
    }

    @Test
    void deleteClient_ShouldReturnNoContent() throws Exception {
        doNothing().when(clientService).deleteByIdentification("77777");

        mockMvc.perform(delete("/api/clients/by-identification/77777"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllClients_ShouldReturnList() throws Exception {
        Client client = new Client();
        client.setIdentificationNumber("99999");
        client.setNames("María");
        client.setLastName("Quintero");
        client.setEmail("maria@mail.com");
        client.setBirthDate(LocalDate.of(1991, 4, 10));

        when(clientService.getAllClients()).thenReturn(List.of(client));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].names").value("María"));
    }
}
