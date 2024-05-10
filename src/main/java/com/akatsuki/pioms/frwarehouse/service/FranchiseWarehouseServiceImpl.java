package com.akatsuki.pioms.frwarehouse.service;


import com.akatsuki.pioms.exchange.aggregate.ExchangeEntity;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProductEntity;
import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProductVO;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.repository.FranchiseWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class FranchiseWarehouseServiceImpl implements FranchiseWarehouseService{
    private final FranchiseWarehouseRepository franchiseWarehouseRepository;

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

    @Override
    @Transactional
    public boolean checkEnableToAddExchange(RequestExchange requestExchange) {
        System.out.println("checkEnableToAddExchange 발생");

        for (int i = 0; i < requestExchange.getProducts().size(); i++) {

            ExchangeProductVO exchange =requestExchange.getProducts().get(i);

            FranchiseWarehouse franchiseWarehouse =
                    franchiseWarehouseRepository.findByProductProductCode(exchange.getProductCode());

            if(franchiseWarehouse!=null && franchiseWarehouse.getFranchiseWarehouseEnable()< exchange.getExchangeProductCount()) {
                System.out.println("error 신청 재고가 너무 많음!");
                return false;
            }
        }
        editCountByPostExchange(requestExchange);
        return true;
    }

    @Transactional
    public void editCountByPostExchange(RequestExchange requestExchange) {
        System.out.println("반송 신청 가능! 재고 수정합니다.");
        int cnt = requestExchange.getProducts().size();
        System.out.println("cnt = " + cnt);
        for (int i = 0; i < cnt; i++) {

            ExchangeProductVO exchange = requestExchange.getProducts().get(i);
            System.out.println(exchange);
            FranchiseWarehouse franchiseWarehouse =
                    franchiseWarehouseRepository.findByProductProductCode(exchange.getProductCode());
            System.out.println(franchiseWarehouse);
            if (franchiseWarehouse!=null) {
                franchiseWarehouse.setFranchiseWarehouseEnable(franchiseWarehouse.getFranchiseWarehouseEnable() - exchange.getExchangeProductCount());
                franchiseWarehouse.setFranchiseWarehouseCount(franchiseWarehouse.getFranchiseWarehouseCount() - exchange.getExchangeProductCount());
                franchiseWarehouseRepository.save(franchiseWarehouse);
            }
        }

    }

}
