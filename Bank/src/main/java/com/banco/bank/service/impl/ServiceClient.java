package com.banco.bank.service.impl;

import com.banco.bank.dto.clientdto.UpdateClientDTO;
import com.banco.bank.mapper.MapperClient;
import com.banco.bank.model.Client;
import com.banco.bank.dto.clientdto.ClientCreateDTO;
import com.banco.bank.repository.RepositoryClient;
import com.banco.bank.service.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ServiceClient implements IClientService {

    private final RepositoryClient repositoryClient;
    private final MapperClient mapperClient;

    private boolean adultValidation(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }

    @Override
    public Client createClient(ClientCreateDTO clientCreateDTO) {
        if (adultValidation(clientCreateDTO.getBirthDate())) {
            return repositoryClient.save(mapperClient.createClient(clientCreateDTO));
        }else
            throw new IllegalArgumentException("You should be over 18 years old");
    }

    @Override
    public Client updateClient(String identificationNumber, UpdateClientDTO updateClientDTO) {
        Client client = repositoryClient.findByIdentificationNumber(identificationNumber).orElseThrow(RuntimeException::new);

        client.setEmail(updateClientDTO.getEmail());
        client.setNames(updateClientDTO.getNames());
        client.setLastName(updateClientDTO.getLastName());
        client.setUpdatedAt(LocalDateTime.now());

        return repositoryClient.save(client);
    }

    @Override
    public void deleteByIdentification(String identificationNumber) {
        Client client = repositoryClient.findByIdentificationNumber(identificationNumber).orElseThrow(() -> new RuntimeException("Client not found"));

        if(client.getAccounts().isEmpty()){
            repositoryClient.delete(client);
        }else
            throw new IllegalArgumentException("Client have a Account");
    }

    @Override
    public Client getClientByIdentificationNumber(String identificationNumber) {
        return repositoryClient.findByIdentificationNumber(identificationNumber).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Client> getAllClients() {
        return repositoryClient.findAll();
    }
}

