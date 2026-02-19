package com.scanex.controllers;

import com.scanex.domain.entities.Address;
import com.scanex.domain.entities.Transaction;
import com.scanex.domain.enums.Chain;
import com.scanex.dtos.TransactionResponseDTO;
import com.scanex.services.AddressService;
import com.scanex.services.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final AddressService addressService;
    private final TransactionService transactionService;

    public TransactionController(AddressService addressService,
                                 TransactionService transactionService) {
        this.addressService = addressService;
        this.transactionService = transactionService;
    }

    @GetMapping("/{chain}/{address}")
    public List<TransactionResponseDTO> getTransactions(
            @PathVariable Chain chain,
            @PathVariable String address,
            @RequestParam(defaultValue = "20") int limit
    ) {
        if (chain != Chain.ETH) {
            return List.of();
        }

        Address a = addressService.getOrCreate(chain, address);
        List<Transaction> txs = transactionService.syncEthTransactions(a, limit);

        return txs.stream()
                .map(t -> new TransactionResponseDTO(
                        t.getId(),
                        t.getHash(),
                        t.getFromAddress(),
                        t.getToAddress(),
                        t.getValue(),
                        t.getTimestamp()
                ))
                .toList();
    }
}
