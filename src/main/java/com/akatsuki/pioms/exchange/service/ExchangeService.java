package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;

public interface ExchangeService {

    ExchangeDTO findExchangeToSend(int franchiseCode);
}
