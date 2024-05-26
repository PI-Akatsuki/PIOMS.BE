package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseDriverInvoice;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.dto.OrderProductDTO;
import com.akatsuki.pioms.order.service.AdminOrderFacade;
import com.akatsuki.pioms.order.service.FranchiseOrderFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InvoiceServiceTest {

    InvoiceService invoiceService;
    InvoiceRepository invoiceRepository;
    AdminOrderFacade orderFacade;
    FranchiseOrderFacade franchiseOrderFacade;

    static int adminCode = 1;
    static int franchiseCode = 1;

    @Autowired
    public InvoiceServiceTest(InvoiceService invoiceService, InvoiceRepository invoiceRepository, AdminOrderFacade orderFacade) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.orderFacade = orderFacade;
    }

//    @Test
//    void postInvoiceAndPut() {
//        // this test located in Order Test
//        // 이 메서드는 주문이 생성된 이후에 바로 이루어지는 것으로 주문 후 확인 가능하다~!
//        int franchiseCode = 1;
//
//        Map<Integer,Integer> requestProducts = new HashMap<>() {{
//            put(1, 1);
//            put(2, 2);
//            put(3, 3);
//        }};
//        RequestOrderVO requestOrderVO = new RequestOrderVO(requestProducts,franchiseCode);
//
//        List<Invoice>  invoices = invoiceRepository.findByOrderFranchiseFranchiseCode(franchiseCode);
//        int invoicesCnt = invoices.size();
//        System.out.println("invoicesCnt = " + invoicesCnt);
//        OrderDTO orderDTO = franchiseOrderFacade.postFranchiseOrder(franchiseCode,requestOrderVO);
//        if (orderDTO==null) {
//            assertEquals(true, true);
//            return;
//        }
//
//        int lastOrderCode = orderDTO.getOrderCode();
//        int adminCode = orderDTO.getAdminCode();
//        OrderDTO orderDTOCmp = orderFacade.acceptOrder(adminCode,lastOrderCode);
//
//        List<Invoice> invoicesCmp = invoiceRepository.findByOrderFranchiseFranchiseCode(franchiseCode);
//        boolean result = invoiceRepository.existsByOrderOrderCode(orderDTO.getOrderCode());
//        System.out.println("invoicesCmp.size() = " + invoicesCmp.size());
//        assertNotEquals(invoicesCnt,invoicesCmp.size());
//        assertEquals(true, result);
//
//
//        // Put
//        //when
//        Invoice invoiceForPut = invoicesCmp.get(invoicesCmp.size()-1);
//        invoiceService.putInvoice(adminCode,invoiceForPut.getInvoiceCode(), DELIVERY_STATUS.배송완료);
//        assertEquals(DELIVERY_STATUS.배송완료, invoiceForPut.getDeliveryStatus());
//    }

    @Test
    @DisplayName(value = "배송 상태 전체조회 테스트 성공")
    void getAllDriverInvoiceList() {

        // given
        List<Invoice> invoiceList = invoiceRepository.findAll();

        // when
        List<ResponseDriverInvoice> responseDriverInvoices = invoiceService.getAllDriverInvoiceList(1);

        // then
        assertEquals(invoiceList.size(), responseDriverInvoices.size());
    }


    @Test
    @DisplayName("배송 상태 상세조회(배송전) 테스트 성공")
    void getStatusBeforeDeliveryDriverInvoiceList() {

        // given
        int driverCode = 1;
        List<ResponseDriverInvoice> invoiceAllList = invoiceService.getAllDriverInvoiceList(driverCode);

        // when
        List<ResponseDriverInvoice> result = invoiceService.getStatusBeforeDeliveryDriverInvoiceList(driverCode);
        System.out.println("result1 = " + result);

        // then
        long expectedCount = invoiceAllList.stream()
                                            .filter(invoiceDTO -> invoiceDTO.getDeliveryStatus() == DELIVERY_STATUS.배송전)
                                            .count();

        assertEquals(expectedCount, result.size());
        for(ResponseDriverInvoice invoiceDTO : result) {
            assertEquals(DELIVERY_STATUS.배송전, invoiceDTO.getDeliveryStatus());
        }
    }

    @Test
    @DisplayName(value = "배송 상태 상세조회(배송중) 테스트 성공")
    void getStatusIngDeliveryDriverInvoiceList() {

        // given
        int driverCode = 1;
        List<ResponseDriverInvoice> invoiceAllList = invoiceService.getAllDriverInvoiceList(driverCode);

        // when
        List<ResponseDriverInvoice> result = invoiceService.getStatusIngDeliveryDriverInvoiceList(driverCode);
        System.out.println("result2 = " + result);

        // then
        long expectedCount = invoiceAllList.stream()
                                            .filter(invoiceDTO -> invoiceDTO.getDeliveryStatus() == DELIVERY_STATUS.배송중)
                                            .count();

        assertEquals(expectedCount, result.size());
        for (ResponseDriverInvoice invoiceDTO : result) {
            assertEquals(DELIVERY_STATUS.배송중, invoiceDTO.getDeliveryStatus());
        }

    }

    @Test
    @DisplayName(value = "배송 상태 상세조회(배송완료) 테스트 성공")
    void getStatusCompleteDeliveryDriverInvoiceList() {

        // given

        // when

        // then

    }

    @Test
    @DisplayName(value = "배송기사가 배송 상태 수정 테스트 성공")
    void modifyInvoiceStatusByDriver() {

        //given

        // when

        //then

    }
}