package com.scanex.integrations.etherscan.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EtherscanTx(
        String hash,
        String from,
        String to,
        String value,
        String timeStamp
) {}
