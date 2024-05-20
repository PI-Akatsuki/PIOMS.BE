package com.akatsuki.pioms.frwarehouse.service;
import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.aggregate.RequestFranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface FranchiseWarehouseService {
    void saveProduct(int productCode, int changeVal, int franchiseCode);

    boolean checkEnableToAddExchange(RequestExchange requestExchange);

    List<FranchiseWarehouseDTO> getAllWarehouse();

    List<FranchiseWarehouseDTO> getWarehouseByWarehouseCode(int franchiseWarehouseCode);

    ResponseEntity<String> updateWarehouseCount(int franchiseWarehouseCode, RequestFranchiseWarehouse request, int requesterAdminCode);

    List<FranchiseWarehouseDTO> getFrWarehouseList(int franchiseOwnerCode);

    void saveProductWhenDeleteExchange(int productCode, int exchangeProductCount, int franchiseCode);
}
