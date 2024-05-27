package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseDriverInvoice;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.dto.OrderProductDTO;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.service.AdminOrderFacade;
import com.akatsuki.pioms.order.service.FranchiseOrderFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private OrderRepository orderRepository;

    static int adminCode = 1;
    static int franchiseCode = 1;


    @Autowired
    public InvoiceServiceTest(InvoiceService invoiceService, InvoiceRepository invoiceRepository, AdminOrderFacade orderFacade, OrderRepository orderRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.orderFacade = orderFacade;
        this.orderRepository = orderRepository;
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
        int driverCode = 1;
        List<ResponseDriverInvoice> invoiceAllList = invoiceService.getAllDriverInvoiceList(driverCode);

        // when
        List<ResponseDriverInvoice> result = invoiceService.getStatusCompleteDeliveryDriverInvoiceList(driverCode);
        System.out.println("result3 = " + result);

        // then
        long expectedCount = invoiceAllList.stream()
                .filter(invoiceDTO -> invoiceDTO.getDeliveryStatus() == DELIVERY_STATUS.배송완료)
                .count();

        assertEquals(expectedCount, result.size());
        for (ResponseDriverInvoice invoiceDTO : result) {
            assertEquals(DELIVERY_STATUS.배송완료, invoiceDTO.getDeliveryStatus());
        }
    }

    @Test
    @DisplayName(value = "배송기사가 배송 상태 수정 테스트 성공")
    void modifyInvoiceStatusByDriver() {

        // given
        int invoiceCode = 25;
        int driverCode = 1;
        int requestCode = 17;
        DELIVERY_STATUS initialStatus = DELIVERY_STATUS.배송전;
        DELIVERY_STATUS updatedStatus = DELIVERY_STATUS.배송중;
        LocalDateTime initialDate = LocalDateTime.parse("2024-05-12T11:23:45"); // ISO 8601 형식으로 변경

        // Order 엔티티 생성 및 설정
        Order order = new Order();
        order.setOrderCode(requestCode);

        orderRepository.save(order);

        Invoice invoice = new Invoice();
        invoice.setInvoiceCode(invoiceCode);
        invoice.setDeliveryStatus(initialStatus);
        invoice.setInvoiceDate(initialDate);
        invoice.setOrder(order); // request_code 설정

        // 미리 저장소에 초기 상태의 인보이스를 저장 (실제 테스트 환경에 따라 달라질 수 있음)
        invoiceRepository.save(invoice);

        // 저장된 송장이 DB에 존재하는지 확인
        Invoice savedInvoice = invoiceRepository.findById(invoiceCode).orElse(null);
        assertNotNull(savedInvoice, "저장된 송장은 null이어서는 안됩니다.");

        // when
        boolean modifyInvoice = invoiceService.modifyInvoiceStatusByDriver(invoiceCode, driverCode, updatedStatus);
        System.out.println("modifyInvoice = " + modifyInvoice);

        // 변경된 인보이스를 다시 조회
        Invoice updatedInvoice = invoiceRepository.findById(invoiceCode).orElse(null);

        // then
        assertNotNull(updatedInvoice, "수정된 송장은 null이어서는 안됩니다.");
        assertEquals(updatedStatus, updatedInvoice.getDeliveryStatus());

    }
}
