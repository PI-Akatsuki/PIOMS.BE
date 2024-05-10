package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.entity.EXCHANGE_PRODUCT_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.exchange.entity.ExchangeProductEntity;
import com.akatsuki.pioms.exchange.entity.RequestExchange;
import com.akatsuki.pioms.exchange.vo.ResponseExchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;

import java.util.List;

public interface ExchangeService {

    ExchangeDTO findExchangeToSend(int franchiseCode);

    List<ResponseExchange> getExchanges();

    ResponseExchange putExchange(int exchangeCode, RequestExchange requestExchange);

    ResponseExchange postExchange(int franchiseCode, RequestExchange requestExchange);

    List<ExchangeProductEntity> getExchangeProducts(int exchangeCode);

    List<ExchangeProductEntity> getExchangeProductsWithStatus(int exchangeCode, EXCHANGE_PRODUCT_STATUS exchangeProductStatus);
}
