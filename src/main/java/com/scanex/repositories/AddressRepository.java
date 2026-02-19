package com.scanex.repositories;

import com.scanex.domain.entities.Address;
import com.scanex.domain.enums.Chain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    Optional<Address> findByChainAndAddress(Chain chain, String address);

    boolean existsByChainAndAddress(Chain chain, String address);
}
