package com.banco.bank.repository;

import com.banco.bank.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RepositoryClient extends JpaRepository<Client,Long> {
    Optional<Client> findByIdentificationNumber(String identificationNumber);
}

