package com.akatsuki.pioms.franchiseWarehouse.service;

import com.akatsuki.pioms.exchange.entity.ExchangeEntity;

public interface FranchiseWarehouseService {
    void saveProduct(int productCocde, int changeVal, int franchiseCode);

    void saveExchangeProduct(ExchangeEntity exchange, int franchiseCode);
}
