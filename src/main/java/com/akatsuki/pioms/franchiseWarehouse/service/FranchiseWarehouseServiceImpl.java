package com.akatsuki.pioms.franchiseWarehouse.service;

import com.akatsuki.pioms.franchiseWarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.franchiseWarehouse.repository.FranchiseWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FranchiseWarehouseServiceImpl implements FranchiseWarehouseService{
    private FranchiseWarehouseRepository franchiseWarehouseRepository;

    @Autowired
    public FranchiseWarehouseServiceImpl(FranchiseWarehouseRepository franchiseWarehouseRepository) {
        this.franchiseWarehouseRepository = franchiseWarehouseRepository;
    }

    public void saveProduct(int productCode, int changeVal, int franchiseCode){
        FranchiseWarehouse franchiseWarehouse
                = franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(productCode,franchiseCode);
        if(franchiseWarehouse == null){
            franchiseWarehouse = new FranchiseWarehouse(false,franchiseCode,productCode);
        }
        franchiseWarehouse.setFranchiseWarehouseTotal(franchiseWarehouse.getFranchiseWarehouseTotal()+changeVal);
        franchiseWarehouse.setFranchiseWarehouseCount(franchiseWarehouse.getFranchiseWarehouseCount()+changeVal);
        franchiseWarehouse.setFranchiseWarehouseEnable(franchiseWarehouse.getFranchiseWarehouseEnable()+changeVal);
        franchiseWarehouseRepository.save(franchiseWarehouse);
        System.out.println("franchiseWarehouse = " + franchiseWarehouse);
    }

    @Override
    @Transactional
    public void toggleFavorite(int warehouseId) {
        FranchiseWarehouse warehouse = franchiseWarehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        warehouse.setFranchiseWarehouseFavorite(!warehouse.isFranchiseWarehouseFavorite());
        franchiseWarehouseRepository.save(warehouse);
    }

    @Override
    public List<FranchiseWarehouse> findAllFavorites() {
        return franchiseWarehouseRepository.findByFranchiseWarehouseFavoriteTrue();
    }

}
