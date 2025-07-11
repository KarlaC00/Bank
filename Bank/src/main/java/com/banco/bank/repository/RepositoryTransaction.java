package com.banco.bank.repository;

import com.banco.bank.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryTransaction extends JpaRepository<Transactions,Long> {
}
