package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.exchange.aggregate.ExchangeEntity;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class InvoiceServiceTest {
    @Autowired
    private InvoiceService invoiceService;
    @MockBean
    private InvoiceRepository invoiceRepository;

    private static ResponseInvoiceList responseInvoiceLists;

    @BeforeAll
    public static void init(){
        responseInvoiceLists = new ResponseInvoiceList();
        responseInvoiceLists.getInvoiceList().add(new ResponseInvoice(1,DELIVERY_STATUS.배송전,LocalDateTime.now(),1,1,
                new ArrayList<>(Stream.of(
                        new OrderProductVO("테스트상품1", 1),
                        new OrderProductVO("테스트상품2", 2)
                ).collect(Collectors.toList())
                )));
    }

    @Test
    public void getAllInvoiceList() {
        // invoiceRespository.findall 결과값을 예상해두기

        when(invoiceRepository.findAll()).thenReturn(
                Stream.of(
                    new InvoiceEntity(1,DELIVERY_STATUS.배송전,LocalDateTime.now(),1
//                            , new Order(1,null, 0,null,null,false,null,null,null)),
                            ,null),
                    new InvoiceEntity(2,DELIVERY_STATUS.배송중,LocalDateTime.now(),1
                            , null)
                ).collect(Collectors.toList())
        );
        ResponseInvoiceList responseInvoiceList = invoiceService.getAllInvoiceList();
        System.out.println(responseInvoiceList);
        assertEquals(2,responseInvoiceList.getInvoiceList().size());
    }

    @Test
    void putInvoice() {

    }

    @Test
    void getInvoice() {
    }

    @Test
    void checkInvoiceStatus() {
    }
}