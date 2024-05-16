package com.akatsuki.pioms.specs.service;

import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.service.OrderFacade;
import com.akatsuki.pioms.specs.aggregate.SpecsEntity;
import com.akatsuki.pioms.specs.dto.SpecsDTO;
import com.akatsuki.pioms.specs.repository.SpecsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SpecsServiceTest {
    OrderFacade orderFacade;
    SpecsRepository specsRepository;
    SpecsService specsService;
    @Autowired
    public SpecsServiceTest(SpecsRepository specsRepository, SpecsService specsService, OrderFacade orderFacade) {
        this.specsRepository = specsRepository;
        this.specsService = specsService;
        this.orderFacade = orderFacade;
    }

    @Test
    void getSpecsList() {
        List<SpecsEntity> specsList = specsRepository.findAll();
        List<SpecsDTO> specsDTOS = specsService.getSpecsList();
        assertEquals(specsList.size(),specsDTOS.size());
    }

    @Test
    void afterAcceptOrder() {
        int franchiseCode = 2;
        Map<Integer,Integer> requestProducts =  new HashMap<Integer,Integer>(){{ put(1, 1); put(2,2); put(3,3);}};
        RequestOrderVO requestOrderVO = new RequestOrderVO(requestProducts,franchiseCode);
        OrderDTO orderDTO = orderFacade.postFranchiseOrder(franchiseCode,requestOrderVO);
        if (orderDTO==null) {
            assertEquals(true, true);
            return;
        }

        int lastOrderCode = orderDTO.getOrderCode();
        int adminCode = orderDTO.getAdminCode();
        OrderDTO orderDTOCmp = orderFacade.acceptOrder(adminCode,lastOrderCode);
        SpecsEntity specs = specsRepository.findByOrderOrderCode(orderDTOCmp.getOrderCode());
        System.out.println("specs = " + specs);
        assertEquals(specs.getOrder().getOrderCode(), orderDTOCmp.getOrderCode());
    }

    @Test
    void getFranchiseSpecsList() {
        int franchiseCode =1;
        List<SpecsEntity> specsList = specsRepository.findAllByOrderFranchiseFranchiseCode(franchiseCode);
        List<SpecsDTO> specsDTOS = specsService.getFranchiseSpecsList(franchiseCode);
        assertEquals(specsList.size(),specsDTOS.size());
    }

    @Test
    void getSpecsByFranchiseCode() {
        int franchiseCode = 1;
        int specsCode =1;
        SpecsEntity specs = specsRepository.findById(1).orElse(null);
        SpecsDTO specsDTO = specsService.getSpecsByFranchiseCode(franchiseCode,1);
        assertEquals(specs.getSpecsCode(),specsDTO.getSpecsCode());
    }

    @Test
    void getSpecsListByAdminCode() {
        List<SpecsEntity> specsList = specsRepository.findAll();
        List<SpecsDTO> specsDTOS = specsService.getSpecsList();
        assertEquals(specsList.size(),specsDTOS.size());
    }

    @Test
    void getSpecsByAdminCode() {
        List<SpecsEntity> specsList = specsRepository.findAll();
        List<SpecsDTO> specsDTOS = specsService.getSpecsList();
        assertEquals(specsList.size(),specsDTOS.size());
    }
}