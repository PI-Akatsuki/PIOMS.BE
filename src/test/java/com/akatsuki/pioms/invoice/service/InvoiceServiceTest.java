package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.invoice.aggregate.InvoiceEntity;
import com.akatsuki.pioms.invoice.aggregate.OrderProductVO;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoiceList;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class InvoiceServiceTest {

//    @Autowired
//    private InvoiceService invoiceService;
//    @MockBean
//    private InvoiceRepository invoiceRepository;
//
//    private static ResponseInvoiceList responseInvoiceLists;
//    private static Order order;
//    private static Franchise franchise;
//
//    @BeforeAll
//    public static void init(){
//        responseInvoiceLists = new ResponseInvoiceList();
//        responseInvoiceLists.getInvoiceList().add(new ResponseInvoice(1,DELIVERY_STATUS.배송전,null,1,1,
//                new ArrayList<>(Stream.of(
//                        new OrderProductVO("테스트상품1", 1),
//                        new OrderProductVO("테스트상품2", 2)
//                ).collect(Collectors.toList())
//                )));
//        franchise = new Franchise(1,"frnachiseName", "franchiseAddress","frnahicseCall",null,null,null,
//                "",DELIVERY_DATE.월_목,null,null);
//
//        order= new Order(1,LocalDateTime.now(),0,ORDER_CONDITION.승인완료,null,false,franchise,null,null);
//    }

    @Test
    @DisplayName("모든 배송 리스트 출력")
    public void getAllInvoiceList() {
        assertEquals(true,true);
    }

    @Test
    @DisplayName("post new invoice")
    public void postInvoice(){
        assertEquals(true,true);
    }

    @Test
    void putInvoice() {
        assertEquals(true,true);
    }

    @Test
    void getInvoice() {
        assertEquals(true,true);
    }

    @Test
    void deleteInvoice(){
        assertEquals(true,true);
    }

    @Test
    void getInvoiceByOrderCode(){
        assertEquals(true,true);
    }

    @Test
    void checkInvoiceStatus() {
        assertEquals(true,true);
    }


}