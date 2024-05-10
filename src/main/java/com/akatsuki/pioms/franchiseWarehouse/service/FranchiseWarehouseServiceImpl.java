package com.akatsuki.pioms.franchiseWarehouse.service;

import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.exchange.entity.ExchangeProductEntity;
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

    @Transactional
    public void saveProduct(int productCode, int changeVal, int franchiseCode){
        FranchiseWarehouse franchiseWarehouse
                = franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(productCode,franchiseCode);
        if(franchiseWarehouse == null){
            franchiseWarehouse = new FranchiseWarehouse(false,franchiseCode,productCode);
        }
        franchiseWarehouse.setFranchiseWarehouseTotal(franchiseWarehouse.getFranchiseWarehouseTotal()+changeVal);
        franchiseWarehouse.setFranchiseWarehouseCount(franchiseWarehouse.getFranchiseWarehouseCount()+changeVal);
        franchiseWarehouse.setFranchiseWarehouseEnable(franchiseWarehouse.getFranchiseWarehouseEnable()+changeVal);
        System.out.println("franchiseWarehouse = " + franchiseWarehouse);
        franchiseWarehouseRepository.save(franchiseWarehouse);
    }

    @Override
    @Transactional
    public void saveExchangeProduct(ExchangeEntity exchange, int franchiseCode) {
        if (exchange==null) return;
        List<ExchangeProductEntity> products = exchange.getProducts();
        products.forEach(product -> {
            int productCode = product.getProduct().getProductCode();
            int cnt = product.getExchangeProductNormalCount();
            saveProduct(productCode,cnt,franchiseCode);
        });
        System.out.println("exchange 처리 완료");
    }

}
