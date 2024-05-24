//package com.akatsuki.pioms.frwarehouse.service;
//
//import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
//import com.akatsuki.pioms.frwarehouse.aggregate.RequestFranchiseWarehouse;
//import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
//import com.akatsuki.pioms.frwarehouse.repository.FranchiseWarehouseRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class FranchiseWarehouseServiceTest {
//
//    @Autowired
//    private FranchiseWarehouseService franchiseWarehouseService;
//
//    @Autowired
//    private FranchiseWarehouseRepository franchiseWarehouseRepository;
//
//
//
//    @Test
//    void getAllWarehouse() {
//        List<FranchiseWarehouse> franchiseWarehouseList = franchiseWarehouseRepository.findAll();
//        List<FranchiseWarehouseDTO> franchiseWarehouseDTOS = franchiseWarehouseService.getAllWarehouse();
//        assertEquals(franchiseWarehouseList.size(),franchiseWarehouseDTOS.size());
//    }
//
//    @Test
//    void getWarehouseByWarehouseCode() {
//        int franchiseWarehouseCode = 9;
//        List<FranchiseWarehouse> franchiseWarehouseList = franchiseWarehouseRepository.findByFranchiseWarehouseCode(franchiseWarehouseCode);
//        List<FranchiseWarehouseDTO> franchiseWarehouseDTOS = franchiseWarehouseService.getWarehouseByWarehouseCode(franchiseWarehouseCode);
//        assertEquals(franchiseWarehouseList.size(),franchiseWarehouseDTOS.size());
//    }
//
//    @Test
//    void updateWarehouseCount() {
//        RequestFranchiseWarehouse request = new RequestFranchiseWarehouse();
//        request.setFranchiseWarehouseCode(9);
//        request.setFranchiseWarehouseTotal(100);
//        request.setFranchiseWarehouseCount(100);
//        request.setFranchiseWarehouseEnable(100);
//        request.setFranchiseWarehouseFavorite(true);
//        request.setFranchiseCode(1);
//        request.setProductCode(1);
//
//        ResponseEntity<String> updateFrWarehouse = franchiseWarehouseService.updateWarehouseCount(request.getFranchiseWarehouseCode(),request,1);
//        System.out.println("updateFrWarehouse = " + updateFrWarehouse);
//
//        assertNotNull(updateFrWarehouse);
//        assertEquals("재고 수정 완료!", updateFrWarehouse.getBody());
//    }
//}
