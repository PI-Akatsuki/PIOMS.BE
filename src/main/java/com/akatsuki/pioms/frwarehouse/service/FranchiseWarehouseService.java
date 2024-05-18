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
    void saveProduct(int productCocde, int changeVal, int franchiseCode);

    void saveExchangeProduct(ExchangeDTO exchange, int franchiseCode);

    boolean checkEnableToAddExchange(RequestExchange requestExchange);

    List<FranchiseWarehouse> getAllWarehouse();

    FranchiseWarehouse getWarehouseByWarehouseCode(int franchiseWarehouseCode);

    ResponseEntity<String> updateWarehouseCount(int franchiseWarehouseCode, RequestFranchiseWarehouseUpdate request, int requesterAdminCode);

    List<FranchiseWarehouseDTO> getFrWarehouseList(int franchiseOwnerCode);
}
