package com.scanex.integrations.etherscan.dtos;

import com.fasterxml.jackson.databind.JsonNode;

public record EtherscanTxlistResponseRaw(
        String status,
        String message,
        JsonNode result
) {}