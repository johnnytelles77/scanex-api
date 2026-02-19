package com.scanex.integrations.etherscan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scanex.exceptions.ExternalProviderException;
import com.scanex.integrations.etherscan.dtos.EtherscanTx;
import com.scanex.integrations.etherscan.dtos.EtherscanTxlistResponse;
import com.scanex.integrations.etherscan.dtos.EtherscanTxlistResponseRaw;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class EtherscanClient {

    private final RestClient restClient;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    public EtherscanClient(
            @Value("${integrations.etherscan.base-url}") String baseUrl,
            @Value("${integrations.etherscan.api-key}") String apiKey,
            ObjectMapper objectMapper
    ) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
    }

    public EtherscanTxlistResponse getNormalTransactionsByAddress(String address, int limit) {
        try {
            String raw = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/api")
                            .queryParam("chainid", 1)
                            .queryParam("module", "account")
                            .queryParam("action", "txlist")
                            .queryParam("address", address)
                            .queryParam("startblock", 0)
                            .queryParam("endblock", 99999999)
                            .queryParam("sort", "desc")
                            .queryParam("page", 1)
                            .queryParam("offset", limit)
                            .queryParam("apikey", apiKey)
                            .build()
                    )
                    .retrieve()
                    .body(String.class);

            if (raw == null || raw.isBlank()) {
                throw new ExternalProviderException("Etherscan returned empty body");
            }

            // 1) Parse como RAW para manejar result: "string" o result: [ ... ]
            EtherscanTxlistResponseRaw parsed = objectMapper.readValue(raw, EtherscanTxlistResponseRaw.class);

            if (parsed == null) {
                throw new ExternalProviderException("Etherscan returned empty response");
            }

            // 2) Si status != 1 => error real del provider
            if (!"1".equals(parsed.status())) {
                String msg = (parsed.result() != null && parsed.result().isTextual())
                        ? parsed.result().asText()
                        : (parsed.message() != null ? parsed.message() : "NOTOK");

                throw new ExternalProviderException("Etherscan NOT OK: " + msg);
            }

            // 3) status OK: result debe ser array
            if (parsed.result() == null || !parsed.result().isArray()) {
                throw new ExternalProviderException("Etherscan OK but result is not an array");
            }

            List<EtherscanTx> txs = objectMapper.convertValue(
                    parsed.result(),
                    new TypeReference<List<EtherscanTx>>() {}
            );

            return new EtherscanTxlistResponse(parsed.status(), parsed.message(), txs);

        } catch (ExternalProviderException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalProviderException("Failed to parse Etherscan response", e);
        }
    }
}