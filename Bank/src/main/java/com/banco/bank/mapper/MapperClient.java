package com.banco.bank.mapper;

import com.banco.bank.dto.clientdto.ClientCreateDTO;
import com.banco.bank.model.Client;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperClient {

    public Client createClient(ClientCreateDTO clientCreateDTO) {
        return Client.builder()
                .names(clientCreateDTO.getNames())
                .lastName(clientCreateDTO.getLastName())
                .identificationNumber(clientCreateDTO.getIdentificationNumber())
                .identificationType(clientCreateDTO.getIdentificationType())
                .email(clientCreateDTO.getEmail())
                .birthDate(clientCreateDTO.getBirthDate())
                .createdAt(LocalDateTime.now())
                .build();
    }
}