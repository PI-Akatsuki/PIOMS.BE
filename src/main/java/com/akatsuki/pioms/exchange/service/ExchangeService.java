package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;

public interface ExchangeService {

    ExchangeDTO findExchangeToSend(int franchiseCode);
}
