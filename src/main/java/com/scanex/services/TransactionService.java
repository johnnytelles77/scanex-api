package com.scanex.services;

import com.scanex.domain.entities.Address;
import com.scanex.domain.entities.Transaction;
import com.scanex.exceptions.ExternalProviderException;
import com.scanex.integrations.etherscan.EtherscanClient;
import com.scanex.integrations.etherscan.dtos.EtherscanTx;
import com.scanex.integrations.etherscan.dtos.EtherscanTxlistResponse;
import com.scanex.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final EtherscanClient etherscanClient;
    private final TransactionRepository transactionRepository;

    public TransactionService(EtherscanClient etherscanClient, TransactionRepository transactionRepository) {
        this.etherscanClient = etherscanClient;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public List<Transaction> syncEthTransactions(Address address, int limit) {

        // 1) Pedimos datos a Etherscan (API externa)
        EtherscanTxlistResponse resp = etherscanClient.getNormalTransactionsByAddress(address.getAddress(), limit);

        if (resp == null) {
            throw new ExternalProviderException("Etherscan returned null response");
        }

        // 2) Validación: Etherscan usa status "1" para OK y "0" para error.
        if (!"1".equals(resp.status())) {
            String msg = (resp.message() == null || resp.message().isBlank()) ? "NOTOK" : resp.message();
            throw new ExternalProviderException("Etherscan NOT OK: " + msg);
        }

        // ✅ CAMBIO: ahora result es List<EtherscanTx> (ya no JsonNode)
        List<EtherscanTx> txs = resp.result();
        if (txs == null || txs.isEmpty()) {
            address.touch();
            return List.of();
        }

        List<Transaction> savedOrExisting = new ArrayList<>();

        // 3) Convertimos + dedupe por hash + guardamos en DB
        for (EtherscanTx tx : txs) {
            if (tx == null || tx.hash() == null || tx.hash().isBlank()) continue;

            // Dedupe: si ya existe en DB, no insertamos de nuevo
            Transaction existing = transactionRepository.findByHash(tx.hash()).orElse(null);
            if (existing != null) {
                savedOrExisting.add(existing);
                continue;
            }

            // Etherscan value viene como string (WEI)
            BigDecimal wei = safeBigDecimal(tx.value());
            Instant timestamp = safeInstantFromSeconds(tx.timeStamp());

            Transaction entity = new Transaction(
                    tx.hash(),
                    tx.from(),
                    tx.to(),
                    wei,
                    timestamp,
                    address
            );

            Transaction saved = transactionRepository.save(entity);
            savedOrExisting.add(saved);
        }

        // 4) Actividad del address (lo “tocamos” porque tuvo sync)
        address.touch();

        return savedOrExisting;
    }

    private BigDecimal safeBigDecimal(String raw) {
        try {
            if (raw == null || raw.isBlank()) return BigDecimal.ZERO;
            return new BigDecimal(raw);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private Instant safeInstantFromSeconds(String seconds) {
        try {
            if (seconds == null || seconds.isBlank()) return Instant.now();
            return Instant.ofEpochSecond(Long.parseLong(seconds));
        } catch (Exception e) {
            return Instant.now();
        }
    }
}