package com.akatsuki.pioms.frwarehouse.service;
import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.aggregate.RequestFranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface FranchiseWarehouseService {
    void saveProduct(int productCode, int changeVal, int franchiseCode);

    boolean checkEnableToAddExchangeAndChangeEnableCnt(RequestExchange requestExchange, int franchiseCode);

    List<FranchiseWarehouseDTO> getAllWarehouse();

    List<FranchiseWarehouseDTO> getWarehouseByWarehouseCode(int franchiseWarehouseCode);

    ResponseEntity<String> updateWarehouseCount(int franchiseWarehouseCode, RequestFranchiseWarehouse request);

    void toggleFavorite(int franchiseWarehouseCode);

    void removeFavorite(int franchiseWarehouseCode);

    List<FranchiseWarehouse> findAllFavorites();

    List<FranchiseWarehouseDTO> getFrWarehouseList();

    void saveProductWhenDeleteExchange(int productCode, int exchangeProductCount, int franchiseCode);

    void saveProductWhenUpdateExchangeToCompany(int productCode, int i, int franchiseCode);

    List<FranchiseWarehouseDTO> getProductsByFranchiseOwnerCode(int franchiseOwnerCode);

    List<FranchiseWarehouseDTO> findFavoritesByOwner(int franchiseOwnerCode);
}

