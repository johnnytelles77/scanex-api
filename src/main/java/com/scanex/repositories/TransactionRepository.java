package com.scanex.repositories;

import com.scanex.domain.entities.Transaction;
import com.scanex.domain.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByHash(String hash);

    List<Transaction> findByAddress(Address address);
}
