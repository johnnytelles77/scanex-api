package com.scanex.integrations.etherscan.dtos;

import java.util.List;

public record EtherscanTxlistResponse(
        String status,
        String message,
        List<EtherscanTx> result
) {}