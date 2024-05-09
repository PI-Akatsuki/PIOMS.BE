package com.akatsuki.pioms.franchiseWarehouse.service;

import com.akatsuki.pioms.franchiseWarehouse.aggregate.FranchiseWarehouse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FranchiseWarehouseService {
    void saveProduct(int productCocde, int changeVal, int franchiseCode);

    @Transactional
    void toggleFavorite(int warehouseId);

    List<FranchiseWarehouse> findAllFavorites();
}
