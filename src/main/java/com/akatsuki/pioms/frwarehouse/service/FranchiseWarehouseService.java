package com.akatsuki.pioms.frwarehouse.service;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.exchange.entity.RequestExchange;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.aggregate.RequestFranchiseWarehouseUpdate;
import com.akatsuki.pioms.frwarehouse.aggregate.ResponseFranchiseWarehouseUpdate;

import java.util.List;
import java.util.Optional;


public interface FranchiseWarehouseService {
    void saveProduct(int productCocde, int changeVal, int franchiseCode);

    void saveExchangeProduct(ExchangeEntity exchange, int franchiseCode);

    boolean checkEnableToAddExchange(RequestExchange requestExchange);

    List<FranchiseWarehouse> getAllWarehouse();

    Optional<FranchiseWarehouse> getWarehouseByWarehouseCode(int franchiseWarehouseCode);

    ResponseFranchiseWarehouseUpdate updateWarehouseCount(int franchiseWarehouseCode, RequestFranchiseWarehouseUpdate request);
}
