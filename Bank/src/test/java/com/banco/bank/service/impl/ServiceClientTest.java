package com.banco.bank.service.impl;

import com.banco.bank.dto.clientdto.ClientCreateDTO;
import com.banco.bank.dto.clientdto.UpdateClientDTO;
import com.banco.bank.mapper.MapperClient;
import com.banco.bank.model.Client;
import com.banco.bank.model.Account;
import com.banco.bank.repository.RepositoryClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceClientTest {

    private RepositoryClient repositoryClient;
    private MapperClient mapperClient;
    private ServiceClient serviceClient;

    @BeforeEach
    void setUp() {
        repositoryClient = mock(RepositoryClient.class);
        mapperClient = mock(MapperClient.class);
        serviceClient = new ServiceClient(repositoryClient, mapperClient);
    }

    @Test
    void testCreateClient_Adult_Success() {
        ClientCreateDTO dto = new ClientCreateDTO();
        dto.setBirthDate(LocalDate.now().minusYears(20));
        dto.setIdentificationType("CC");
        dto.setIdentificationNumber("1234567890");
        dto.setNames("John");
        dto.setLastName("Doe");
        dto.setEmail("john@example.com");

        Client client = new Client();
        when(mapperClient.createClient(dto)).thenReturn(client);

        String result = serviceClient.createClient(dto);

        assertEquals("Client created successfully", result);
        verify(repositoryClient).save(client);
    }

    @Test
    void testCreateClient_Minor_ThrowsException() {
        ClientCreateDTO dto = new ClientCreateDTO();
        dto.setBirthDate(LocalDate.now().minusYears(15));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                serviceClient.createClient(dto));

        assertEquals("You should be over 18 years old", exception.getMessage());
        verify(repositoryClient, never()).save(any());
    }

    @Test
    void testUpdateClient_Success() {
        String idNumber = "1234567890";
        UpdateClientDTO updateDto = new UpdateClientDTO();
        updateDto.setNames("UpdatedName");
        updateDto.setLastName("UpdatedLast");
        updateDto.setEmail("updated@example.com");

        Client existingClient = new Client();
        when(repositoryClient.findByIdentificationNumber(idNumber)).thenReturn(Optional.of(existingClient));
        when(repositoryClient.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Client updated = serviceClient.updateClient(idNumber, updateDto);

        assertEquals("UpdatedName", updated.getNames());
        assertEquals("UpdatedLast", updated.getLastName());
        assertEquals("updated@example.com", updated.getEmail());
        assertNotNull(updated.getUpdatedAt());
    }

    @Test
    void testUpdateClient_NotFound_ThrowsException() {
        when(repositoryClient.findByIdentificationNumber("404")).thenReturn(Optional.empty());

        UpdateClientDTO dto = new UpdateClientDTO(); 

        assertThrows(RuntimeException.class, () ->
                serviceClient.updateClient("404", dto));
    }


    @Test
    void testDeleteClient_NoAccounts_Success() {
        Client client = new Client();
        client.setAccounts(Collections.emptyList());

        when(repositoryClient.findByIdentificationNumber("123")).thenReturn(Optional.of(client));

        serviceClient.deleteByIdentification("123");

        verify(repositoryClient).delete(client);
    }

    @Test
    void testDeleteClient_WithAccounts_ThrowsException() {
        Client client = new Client();
        client.setAccounts(List.of(mock(Account.class)));

        when(repositoryClient.findByIdentificationNumber("123")).thenReturn(Optional.of(client));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                serviceClient.deleteByIdentification("123"));

        assertEquals("Client have a Account", exception.getMessage());
        verify(repositoryClient, never()).delete(any());
    }

    @Test
    void testDeleteClient_NotFound_ThrowsException() {
        when(repositoryClient.findByIdentificationNumber("999")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                serviceClient.deleteByIdentification("999"));

        assertEquals("Client not found", exception.getMessage());
    }

    @Test
    void testGetClientByIdentificationNumber_Success() {
        Client client = new Client();
        when(repositoryClient.findByIdentificationNumber("123")).thenReturn(Optional.of(client));

        Client result = serviceClient.getClientByIdentificationNumber("123");

        assertEquals(client, result);
    }

    @Test
    void testGetClientByIdentificationNumber_NotFound() {
        when(repositoryClient.findByIdentificationNumber("123")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                serviceClient.getClientByIdentificationNumber("123"));
    }

    @Test
    void testGetAllClients() {
        List<Client> clients = List.of(new Client(), new Client());
        when(repositoryClient.findAll()).thenReturn(clients);

        List<Client> result = serviceClient.getAllClients();

        assertEquals(2, result.size());
    }
}
