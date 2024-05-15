package com.akatsuki.pioms.frwarehouse.service;


import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.exchange.aggregate.Exchange;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProduct;
import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProductVO;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.aggregate.RequestFranchiseWarehouseUpdate;
import com.akatsuki.pioms.frwarehouse.aggregate.ResponseFranchiseWarehouseUpdate;
import com.akatsuki.pioms.frwarehouse.repository.FranchiseWarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class FranchiseWarehouseServiceImpl implements FranchiseWarehouseService{
    private final FranchiseWarehouseRepository franchiseWarehouseRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public FranchiseWarehouseServiceImpl(FranchiseWarehouseRepository franchiseWarehouseRepository, AdminRepository adminRepository) {
        this.franchiseWarehouseRepository = franchiseWarehouseRepository;
        this.adminRepository = adminRepository;
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
    public void saveExchangeProduct(Exchange exchange, int franchiseCode) {
        if (exchange==null) return;
        List<ExchangeProduct> products = exchange.getProducts();
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
                    franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(exchange.getProductCode(), requestExchange.getFranchiseCode());

            if(franchiseWarehouse==null || franchiseWarehouse.getFranchiseWarehouseEnable()< exchange.getExchangeProductCount()) {
                System.out.println("error 신청 재고가 너무 많음!");
                System.out.println("franchiseWarehouse.getFranchiseWarehouseEnable() = " + franchiseWarehouse.getFranchiseWarehouseEnable());
                System.out.println("exchange = " + exchange.getExchangeProductCount());
                return false;
            }
        }
        editCountByPostExchange(requestExchange);
        return true;
    }

    @Override
    @Transactional
    public List<FranchiseWarehouse> getAllWarehouse() {
        return franchiseWarehouseRepository.findAll();
    }

    @Override
    @Transactional
    public FranchiseWarehouse getWarehouseByWarehouseCode(int franchiseWarehouseCode) {
        return franchiseWarehouseRepository.findById(franchiseWarehouseCode).orElseThrow(null);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateWarehouseCount(int franchiseWarehouseCode, RequestFranchiseWarehouseUpdate request, int requesterAdminCode) {
        Optional<Admin> requestorAdmin = adminRepository.findById(requesterAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("신규 카테고리 등록은 루트 관리자만 가능합니다.");
        }
        FranchiseWarehouse franchiseWarehouse = franchiseWarehouseRepository.findById(franchiseWarehouseCode)
                .orElseThrow(() -> new EntityNotFoundException("FranchiseWarehouse not found"));

        franchiseWarehouse.setFranchiseWarehouseTotal(request.getFranchiseWarehouseTotal());
        franchiseWarehouse.setFranchiseWarehouseEnable(request.getFranchiseWarehouseEnable());
        franchiseWarehouse.setFranchiseWarehouseCount(request.getFranchiseWarehouseCount());

        franchiseWarehouseRepository.save(franchiseWarehouse);
        return ResponseEntity.ok("재고 수정 완료!");

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
                    franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(exchange.getProductCode(),requestExchange.getFranchiseCode());
            System.out.println(franchiseWarehouse);
            if (franchiseWarehouse!=null) {
                franchiseWarehouse.setFranchiseWarehouseEnable(franchiseWarehouse.getFranchiseWarehouseEnable() - exchange.getExchangeProductCount());
                franchiseWarehouse.setFranchiseWarehouseCount(franchiseWarehouse.getFranchiseWarehouseCount() - exchange.getExchangeProductCount());
                franchiseWarehouseRepository.save(franchiseWarehouse);
            }
        }

    }

}
