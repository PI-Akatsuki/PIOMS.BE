package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.exchange.aggregate.ExchangeEntity;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class InvoiceServiceTest {
    @Autowired
    private InvoiceService invoiceService;
    @MockBean
    private InvoiceRepository invoiceRepository;

    private static ResponseInvoiceList responseInvoiceLists;
    private static Order order;
    private static Franchise franchise;

    @BeforeAll
    public static void init(){
        responseInvoiceLists = new ResponseInvoiceList();
        responseInvoiceLists.getInvoiceList().add(new ResponseInvoice(1,DELIVERY_STATUS.배송전,null,1,1,
                new ArrayList<>(Stream.of(
                        new OrderProductVO("테스트상품1", 1),
                        new OrderProductVO("테스트상품2", 2)
                ).collect(Collectors.toList())
                )));
        franchise = new Franchise(1,"frnachiseName", "franchiseAddress","frnahicseCall",null,null,null,
                DELIVERY_DATE.월_목,null,null,0);

        order= new Order(1,LocalDateTime.now(),0,ORDER_CONDITION.승인완료,null,false,franchise,null,null);
    }

    @Test
    @DisplayName("모든 배송 리스트 출력")
    public void getAllInvoiceList() {
        //given
        when(invoiceRepository.findAll())
                .thenReturn(
                Stream.of(
                    new InvoiceEntity(1,DELIVERY_STATUS.배송전,null,1
//                            , new Order(1,null, 0,null,null,false,null,null,null)),
                            ,null),
                    new InvoiceEntity(2,DELIVERY_STATUS.배송중,null,1
                            , null)
                ).collect(Collectors.toList())
        );
        //when
        ResponseInvoiceList responseInvoiceList = invoiceService.getAllInvoiceList();
        System.out.println(responseInvoiceList);
        //then
        assertEquals(2,responseInvoiceList.getInvoiceList().size());
    }

    @Test
    @DisplayName("post new invoice")
    public void postInvoice(){
        //when
        InvoiceEntity invoiceEntity =
                new InvoiceEntity(1,DELIVERY_STATUS.배송전,null,1,null);
        when(invoiceRepository.save(invoiceEntity)).thenReturn(
          invoiceEntity
        );
//        InvoiceEntity invoiceEntityCmp = invoiceService.postInvoice(order);
//        System.out.println("invoiceEntityCmp = " + invoiceEntityCmp);
        InvoiceEntity invoice = invoiceService.saveInvoice(invoiceEntity);
        assertEquals(invoiceEntity, invoice);
    }

    @Test
    void putInvoice() {
        //given
        InvoiceEntity invoiceEntity =
                new InvoiceEntity(1,DELIVERY_STATUS.배송전,null,1,null);
        when(invoiceRepository.save(invoiceEntity)).thenReturn(
                invoiceEntity
        );

        //when
//        assertEquals(invoiceEntity,);

    }

    @Test
    void getInvoice() {
        int invoiceCode = 1;
        when(invoiceRepository.findById(invoiceCode) )
                .thenReturn(
                        Optional.of(new InvoiceEntity(1, DELIVERY_STATUS.배송전, null, 1, null))
                );
        assertEquals(1, invoiceService.getInvoice(1).getInvoiceCode());
    }

    @Test
    void deleteInvoice(){
        InvoiceEntity invoiceEntity =
                new InvoiceEntity(1,DELIVERY_STATUS.배송전,null,1,null);
        invoiceService.deleteInvoice(invoiceEntity);
        verify(invoiceRepository,times(1)).delete(invoiceEntity);
    }

    @Test
    void checkInvoiceStatus() {
    }
}