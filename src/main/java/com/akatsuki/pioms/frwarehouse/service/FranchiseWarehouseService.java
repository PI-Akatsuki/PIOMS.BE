package com.akatsuki.pioms.frwarehouse.service;
import com.akatsuki.pioms.exchange.aggregate.ExchangeEntity;
import com.akatsuki.pioms.exchange.aggregate.RequestExchange;

public interface FranchiseWarehouseService {
    void saveProduct(int productCocde, int changeVal, int franchiseCode);

    void saveExchangeProduct(ExchangeEntity exchange, int franchiseCode);

    boolean checkEnableToAddExchange(RequestExchange requestExchange);
}
