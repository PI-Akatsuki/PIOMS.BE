package com.akatsuki.pioms.frwarehouse.service;
import com.akatsuki.pioms.exchange.aggregate.Exchange;
import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.aggregate.RequestFranchiseWarehouseUpdate;
import com.akatsuki.pioms.frwarehouse.aggregate.ResponseFranchiseWarehouseUpdate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


public interface FranchiseWarehouseService {
    void saveProduct(int productCode, int changeVal, int franchiseCode);

    void saveExchangeProduct(ExchangeDTO exchange, int franchiseCode);

    boolean checkEnableToAddExchange(RequestExchange requestExchange);

    List<FranchiseWarehouse> getAllWarehouse();

    FranchiseWarehouse getWarehouseByWarehouseCode(int franchiseWarehouseCode);

    ResponseEntity<String> updateWarehouseCount(int franchiseWarehouseCode, RequestFranchiseWarehouseUpdate request, int requesterAdminCode);

    void toggleFavorite(int franchiseWarehouseCode);

    List<FranchiseWarehouse> findAllFavorites();

    List<FranchiseWarehouseDTO> getFrWarehouseList(int franchiseOwnerCode);

    void saveProductWhenDeleteExchange(int productCode, int exchangeProductCount, int franchiseCode);
}
