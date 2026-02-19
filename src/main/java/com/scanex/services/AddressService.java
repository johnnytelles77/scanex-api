package com.scanex.services;

import com.scanex.domain.entities.Address;
import com.scanex.domain.enums.Chain;
import com.scanex.repositories.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public Address getOrCreate(Chain chain, String addressValue) {
        // Normalización mínima (por ahora)
        String normalized = addressValue.trim();

        return addressRepository.findByChainAndAddress(chain, normalized)
                .map(existing -> {
                    existing.touch();          // actualiza lastSeen
                    return existing;
                })
                .orElseGet(() -> addressRepository.save(new Address(chain, normalized)));
    }
}