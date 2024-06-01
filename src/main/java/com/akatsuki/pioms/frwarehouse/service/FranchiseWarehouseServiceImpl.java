package com.akatsuki.pioms.frwarehouse.service;


import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProductVO;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.aggregate.RequestFranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
import com.akatsuki.pioms.frwarehouse.repository.FranchiseWarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class FranchiseWarehouseServiceImpl implements FranchiseWarehouseService{
    private final FranchiseWarehouseRepository franchiseWarehouseRepository;
    private final AdminRepository adminRepository;
    private final FranchiseService franchiseService;

    @Autowired
    public FranchiseWarehouseServiceImpl(FranchiseWarehouseRepository franchiseWarehouseRepository, AdminRepository adminRepository,FranchiseService franchiseService) {
        this.franchiseWarehouseRepository = franchiseWarehouseRepository;
        this.adminRepository = adminRepository;
        this.franchiseService = franchiseService;
    }

    @Transactional
    @Override
    public void saveProduct(int productCode, int changeVal, int franchiseCode){
        System.out.println("가맹창고접근");
        FranchiseWarehouse franchiseWarehouse
                = franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(productCode,franchiseCode);
        System.out.println("franchiseWarehouse = " + franchiseWarehouse);
        //없다면 새로 저장하긔
        if(franchiseWarehouse == null ){
            franchiseWarehouse = new FranchiseWarehouse(false,franchiseCode,productCode);
        }
        if(changeVal>=0)
            franchiseWarehouse.setFranchiseWarehouseTotal(franchiseWarehouse.getFranchiseWarehouseTotal()+changeVal);

        franchiseWarehouse.setFranchiseWarehouseCount(franchiseWarehouse.getFranchiseWarehouseCount()+changeVal);
        franchiseWarehouse.setFranchiseWarehouseEnable(franchiseWarehouse.getFranchiseWarehouseEnable()+changeVal);
        franchiseWarehouseRepository.save(franchiseWarehouse);
        System.out.println("saved");
    }

    @Transactional
    @Override
    public void saveProductWhenDeleteExchange(int productCode, int changeVal, int franchiseCode){
        FranchiseWarehouse franchiseWarehouse
                = franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(productCode,franchiseCode);
        franchiseWarehouse.setFranchiseWarehouseEnable(franchiseWarehouse.getFranchiseWarehouseEnable()+changeVal);
        franchiseWarehouseRepository.save(franchiseWarehouse);
    }

    @Override
    public void saveProductWhenUpdateExchangeToCompany(int productCode, int changeVal, int franchiseCode) {
        FranchiseWarehouse franchiseWarehouse
                = franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(productCode,franchiseCode);
        franchiseWarehouse.setFranchiseWarehouseCount(franchiseWarehouse.getFranchiseWarehouseCount()-changeVal);
        franchiseWarehouseRepository.save(franchiseWarehouse);
    }

    @Override
    @Transactional
    public boolean checkEnableToAddExchangeAndChangeEnableCnt(RequestExchange requestExchange, int franchiseCode) {

        for (int i = 0; i < requestExchange.getProducts().size(); i++) {
            ExchangeProductVO exchange =requestExchange.getProducts().get(i);
            FranchiseWarehouse franchiseWarehouse =
                    franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(exchange.getProductCode(), franchiseCode);
            if(franchiseWarehouse==null || franchiseWarehouse.getFranchiseWarehouseEnable()< exchange.getExchangeProductCount()) {
                return false;
            }
        }
        editCountByPostExchange(requestExchange, franchiseCode);
        return true;
    }
    @Transactional
    public void editCountByPostExchange(RequestExchange requestExchange,int franchiseCode) {
        int cnt = requestExchange.getProducts().size();
        for (int i = 0; i < cnt; i++) {
            ExchangeProductVO exchange = requestExchange.getProducts().get(i);
            FranchiseWarehouse franchiseWarehouse =
                    franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(exchange.getProductCode(),franchiseCode);
            System.out.println(franchiseWarehouse);
            if (franchiseWarehouse!=null) {
                franchiseWarehouse.setFranchiseWarehouseEnable(franchiseWarehouse.getFranchiseWarehouseEnable() - exchange.getExchangeProductCount());
                System.out.println("sadfjfsddfbshsdfbj");
                System.out.println("franchiseWarehouse = " + franchiseWarehouse);
                franchiseWarehouseRepository.save(franchiseWarehouse);
            }
        }
    }

    @Override
    @Transactional
    public List<FranchiseWarehouseDTO> getAllWarehouse() {
        List<FranchiseWarehouse> franchiseWarehouseList = franchiseWarehouseRepository.findAll();
        List<FranchiseWarehouseDTO> responseFrWarehouse = new ArrayList<>();

        franchiseWarehouseList.forEach(franchiseWarehouse -> {
            responseFrWarehouse.add(new FranchiseWarehouseDTO(franchiseWarehouse));
        });
        return responseFrWarehouse;
    }

    @Override
    @Transactional
    public List<FranchiseWarehouseDTO> getWarehouseByWarehouseCode(int franchiseWarehouseCode) {
        List<FranchiseWarehouse> franchiseWarehouseList = franchiseWarehouseRepository.findByFranchiseWarehouseCode(franchiseWarehouseCode);
        List<FranchiseWarehouseDTO> franchiseWarehouseDTOS = new ArrayList<>();
        franchiseWarehouseList.forEach(franchiseWarehouse -> {
            franchiseWarehouseDTOS.add(new FranchiseWarehouseDTO(franchiseWarehouse));
        });
        return franchiseWarehouseDTOS;
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateWarehouseCount(int franchiseWarehouseCode, RequestFranchiseWarehouse request, int requesterAdminCode) {
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

    @Override
    public List<FranchiseWarehouseDTO> getFrWarehouseList() {
//        int franchiseOwnerCode = convertUser.getCode();
        int franchiseOwnerCode = 1;


        int franchiseCode = franchiseService.findFranchiseByFranchiseOwnerCode(franchiseOwnerCode).getFranchiseCode();
        List<FranchiseWarehouse> franchiseWarehouses = franchiseWarehouseRepository.findAllByFranchiseCode(franchiseCode);
        List<FranchiseWarehouseDTO> franchiseWarehouseDTOS = new ArrayList<>();
        for (int i = 0; i < franchiseWarehouses.size(); i++) {
            franchiseWarehouseDTOS.add(new FranchiseWarehouseDTO(franchiseWarehouses.get(i)));
        }
        return franchiseWarehouseDTOS;
    }



    @Override
    @Transactional
    public void toggleFavorite(int franchiseWarehouseCode) {
        FranchiseWarehouse favorite = franchiseWarehouseRepository.findById(franchiseWarehouseCode)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        if (favorite.isFranchiseWarehouseFavorite()) {
            throw new RuntimeException("이미 즐겨찾기 추가된 상품입니다");
        }

        favorite.setFranchiseWarehouseFavorite(true);
        franchiseWarehouseRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(int franchiseWarehouseCode) {
        FranchiseWarehouse favorite = franchiseWarehouseRepository.findById(franchiseWarehouseCode)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        if (!favorite.isFranchiseWarehouseFavorite()) {
            throw new RuntimeException("즐겨찾기 추가되지 않은 상품입니다");
        }

        favorite.setFranchiseWarehouseFavorite(false);
        franchiseWarehouseRepository.save(favorite);
    }

    @Override
    public List<FranchiseWarehouse> findAllFavorites() {
        return franchiseWarehouseRepository.findByFranchiseWarehouseFavoriteTrue();
    }
}
