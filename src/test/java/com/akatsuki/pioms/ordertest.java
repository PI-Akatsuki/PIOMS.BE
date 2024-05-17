package com.akatsuki.pioms;

import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.service.OrderFacade;
import com.akatsuki.pioms.specs.repository.SpecsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ordertest {

    OrderFacade orderFacade;
    ExchangeService exchangeService;

    OrderRepository orderRepository;
    InvoiceRepository invoiceRepository;
    ExchangeRepository exchangeRepository;
    SpecsRepository specsRepository;

    public ordertest(OrderFacade orderFacade, ExchangeService exchangeService, OrderRepository orderRepository, InvoiceRepository invoiceRepository, ExchangeRepository exchangeRepository, SpecsRepository specsRepository) {
        this.orderFacade = orderFacade;
        this.exchangeService = exchangeService;
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.exchangeRepository = exchangeRepository;
        this.specsRepository = specsRepository;
    }

    @Test
    @DisplayName("주문 통합 테스트")
    void Test(){
        // 1. 교환 등록
        // 1-1. 교환 생성
        // 1-2. 교환 삭제
        // 1-3. 교환 생성

        // 2. 발주 등록
        // 2-1. 발주 생성
        // 2-2. 발주 수정
        // 2-3. 발주 거절
        // 2-4. 발주



    }

}
