package com.banco.bank.controller;
import com.banco.bank.dto.clientdto.ClientCreateDTO;
import com.banco.bank.dto.clientdto.UpdateClientDTO;
import com.banco.bank.model.Client;
import com.banco.bank.service.IClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final IClientService serviceClient; //Buscar una clase service q tenga esa interfaz lol

    public ClientController(IClientService serviceClient) {
        this.serviceClient = serviceClient;
    }

    @PostMapping
    public String createClient(@Valid @RequestBody ClientCreateDTO clientCreateDTO) {
        return serviceClient.createClient(clientCreateDTO);
    }

    @PutMapping("/by-identification/{identificationNumber}")
    public ResponseEntity<Client> updateClient(@Valid @PathVariable String identificationNumber, @RequestBody UpdateClientDTO updateClientDTO) {
        return ResponseEntity.ok().body(serviceClient.updateClient(identificationNumber, updateClientDTO));
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok().body(serviceClient.getAllClients());
    }

    @DeleteMapping("/by-identification/{identificationNumber}")
    public ResponseEntity<Void> deleteClient(@PathVariable String identificationNumber) {
        serviceClient.deleteByIdentification(identificationNumber);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-identification/{identificationNumber}")
    public ResponseEntity<Client> getClientById(@PathVariable String identificationNumber) {
        Client client = serviceClient.getClientByIdentificationNumber(identificationNumber);
        return ResponseEntity.ok().body(client);
    }
}