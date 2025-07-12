package com.banco.bank.service;

import java.util.List;
import com.banco.bank.dto.clientdto.ClientCreateDTO;
import com.banco.bank.dto.clientdto.UpdateClientDTO;
import com.banco.bank.model.Client;

public interface IClientService {
    Client createClient(ClientCreateDTO clientCreateDTO);
    Client updateClient(String identificationNumber, UpdateClientDTO updateClientDTO);
    Client getClientByIdentificationNumber(String identificationNumber);
    List<Client> getAllClients();
    void deleteByIdentification(String identificationNumber);
}