package com.akatsuki.pioms.frwarehouse.service;

import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.repository.FranchiseWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
