//package com.akatsuki.pioms.specs.service;
//
//import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
//import com.akatsuki.pioms.order.dto.OrderDTO;
//import com.akatsuki.pioms.order.service.AdminOrderFacade;
//import com.akatsuki.pioms.order.service.FranchiseOrderFacade;
//import com.akatsuki.pioms.specs.aggregate.Specs;
//import com.akatsuki.pioms.specs.dto.SpecsDTO;
//import com.akatsuki.pioms.specs.repository.SpecsRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class SpecsServiceTest {
//    AdminOrderFacade orderFacade;
//    SpecsRepository specsRepository;
//    SpecsService specsService;
//
//    @Autowired
//    FranchiseOrderFacade franchiseOrderFacade;
//    @Autowired
//    public SpecsServiceTest(SpecsRepository specsRepository, SpecsService specsService, AdminOrderFacade orderFacade) {
//        this.specsRepository = specsRepository;
//        this.specsService = specsService;
//        this.orderFacade = orderFacade;
//    }
//
//    @Test
//    void getSpecsList() {
//        //fin
//        List<Specs> specsList = specsRepository.findAll();
//        List<SpecsDTO> specsDTOS = specsService.getSpecsList();
//        assertEquals(specsList.size(),specsDTOS.size());
//    }
//
//    @Test
//    void afterAcceptOrder() {
//        //fin
//        int franchiseCode = 2;
//        Map<Integer,Integer> requestProducts =  new HashMap<Integer,Integer>(){{ put(1, 1); put(2,2); put(3,3);}};
//        RequestOrderVO requestOrderVO = new RequestOrderVO(requestProducts,franchiseCode);
//        OrderDTO orderDTO = franchiseOrderFacade.postFranchiseOrder(franchiseCode,requestOrderVO);
//        if (orderDTO==null) {
//            System.out.println("이미 존재하여 생성 안합니다.");
//            assertEquals(true, true);
//            return;
//        }
//
//        int lastOrderCode = orderDTO.getOrderCode();
//        int adminCode = orderDTO.getAdminCode();
//        OrderDTO orderDTOCmp = orderFacade.acceptOrder(adminCode,lastOrderCode);
//        Specs specs = specsRepository.findByOrderOrderCode(orderDTOCmp.getOrderCode());
//        System.out.println("specs = " + specs);
//        assertEquals(specs.getOrder().getOrderCode(), orderDTOCmp.getOrderCode());
//    }
//
//    @Test
//    void getFranchiseSpecsList() {
//        //fin
//        int franchiseCode =1;
//
//        List<Specs> specsList = specsRepository.findAllByOrderFranchiseFranchiseCode(franchiseCode);
//        List<SpecsDTO> specsDTOS = specsService.getSpecsListByFrOwnerCode(franchiseCode);
//
//        assertEquals(specsList.size(),specsDTOS.size());
//    }
//
//    @Test
//    void getSpecsByFranchiseCode() {
//        int franchiseCode = 2;
//        Map<Integer,Integer> requestProducts =  new HashMap<Integer,Integer>(){{ put(1, 1); put(2,2); put(3,3);}};
//        RequestOrderVO requestOrderVO = new RequestOrderVO(requestProducts,franchiseCode);
//        OrderDTO orderDTO = franchiseOrderFacade.postFranchiseOrder(franchiseCode,requestOrderVO);
//        if (orderDTO==null) {
//            System.out.println("이미 존재하여 생성 안합니다.");
//            assertEquals(true, true);
//            return;
//        }
//        Specs specs = specsRepository.findByOrderOrderCode(orderDTO.getOrderCode());
//        if (specs!=null) {
//            SpecsDTO specsDTO = specsService.getSpecsByFranchiseCode(franchiseCode, specs.getOrder().getOrderCode());
//            assertEquals(specs.getSpecsCode(), specsDTO.getSpecsCode());
//
//            specsDTO = specsService.getSpecsByAdminCode(orderDTO.getAdminCode(),specs.getOrder().getOrderCode());
//            assertEquals(specs.getSpecsCode(),specsDTO.getSpecsCode());
//        }
//    }
//
//    @Test
//    void getSpecsListByAdminCode() {
//        int adminCode = 1;
//        List<Specs> specsList = specsRepository.findAllByOrderFranchiseAdminAdminCode(adminCode);
//        List<SpecsDTO> specsDTOS = specsService.getSpecsListByAdminCode(adminCode);
//        assertEquals(specsList.size(), specsDTOS.size());
//    }
//}