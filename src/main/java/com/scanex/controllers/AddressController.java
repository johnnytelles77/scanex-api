package com.scanex.controllers;

import com.scanex.domain.entities.Address;
import com.scanex.domain.enums.Chain;
import com.scanex.dtos.AddressResponseDTO;
import com.scanex.services.AddressService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{chain}/{address}")
    public AddressResponseDTO getAddress(
            @PathVariable Chain chain,
            @PathVariable String address
    ) {
        Address saved = addressService.getOrCreate(chain, address);

        return new AddressResponseDTO(
                saved.getId(),
                saved.getChain(),
                saved.getAddress(),
                saved.getFirstSeen(),
                saved.getLastSeen()
        );
    }
}
