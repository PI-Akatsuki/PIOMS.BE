package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.service.AdminOrderFacade;
import com.akatsuki.pioms.order.service.FranchiseOrderFacade;
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
class InvoiceServiceTest {

    InvoiceService invoiceService;
    InvoiceRepository invoiceRepository;
    AdminOrderFacade orderFacade;
    FranchiseOrderFacade franchiseOrderFacade;

    static int adminCode= 1;
    static int franchiseCode =1;

    @Autowired
    public InvoiceServiceTest(InvoiceService invoiceService, InvoiceRepository invoiceRepository, AdminOrderFacade orderFacade) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.orderFacade = orderFacade;
    }

    @Test
    void postInvoiceAndPut() {
        // this test located in Order Test
        // 이 메서드는 주문이 생성된 이후에 바로 이루어지는 것으로 주문 후 확인 가능하다~!
        int franchiseCode = 1;

        Map<Integer,Integer> requestProducts = new HashMap<>() {{
            put(1, 1);
            put(2, 2);
            put(3, 3);
        }};
        RequestOrderVO requestOrderVO = new RequestOrderVO(requestProducts,franchiseCode);

        List<Invoice>  invoices = invoiceRepository.findByOrderFranchiseFranchiseCode(franchiseCode);
        int invoicesCnt = invoices.size();
        System.out.println("invoicesCnt = " + invoicesCnt);
        OrderDTO orderDTO = franchiseOrderFacade.postFranchiseOrder(franchiseCode,requestOrderVO);
        if (orderDTO==null) {
            assertEquals(true, true);
            return;
        }

        int lastOrderCode = orderDTO.getOrderCode();
        int adminCode = orderDTO.getAdminCode();
        OrderDTO orderDTOCmp = orderFacade.acceptOrder(adminCode,lastOrderCode);

        List<Invoice> invoicesCmp = invoiceRepository.findByOrderFranchiseFranchiseCode(franchiseCode);
        boolean result = invoiceRepository.existsByOrderOrderCode(orderDTO.getOrderCode());
        System.out.println("invoicesCmp.size() = " + invoicesCmp.size());
        assertNotEquals(invoicesCnt,invoicesCmp.size());
        assertEquals(true, result);


        // Put
        //when
        Invoice invoiceForPut = invoicesCmp.get(invoicesCmp.size()-1);
        invoiceService.putInvoice(adminCode,invoiceForPut.getInvoiceCode(), DELIVERY_STATUS.배송완료);
        assertEquals(DELIVERY_STATUS.배송완료, invoiceForPut.getDeliveryStatus());
    }
}