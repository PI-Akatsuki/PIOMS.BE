package com.akatsuki.pioms;

import com.akatsuki.pioms.exchange.aggregate.*;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.dto.ExchangeProductDTO;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.aggregate.RequestPutOrder;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.service.OrderFacade;
import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.specs.aggregate.Specs;
import com.akatsuki.pioms.specs.repository.SpecsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootTest
@Transactional
public class ordertest {

    @Autowired
    OrderFacade orderFacade;
    @Autowired
    ExchangeService exchangeService;
    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    ExchangeRepository exchangeRepository;
    @Autowired
    SpecsRepository specsRepository;
    @Autowired
    FranchiseService franchiseService;

    static List<ExchangeProductVO> exchangeProductVOS;
    static RequestExchange exchange;

    @Test
    @DisplayName("주문 통합 테스트")
    void Test(){
        // 1. 교환 등록
        int adminCode=1;
        int franchiseCode=1;
        Franchise franchise = franchiseService.findFranchiseById(franchiseCode).orElseThrow();

        exchangeProductVOS = Stream.of(
                new ExchangeProductVO(1, 2,EXCHANGE_PRODUCT_STATUS.교환),
                new ExchangeProductVO(2, 3,EXCHANGE_PRODUCT_STATUS.폐기),
                new ExchangeProductVO(3, 4,EXCHANGE_PRODUCT_STATUS.교환),
                new ExchangeProductVO(4, 5,EXCHANGE_PRODUCT_STATUS.폐기)
        ).toList();
        exchange = new RequestExchange(1,EXCHANGE_STATUS.반송신청,exchangeProductVOS);
        // 1-1. 교환 생성
        ExchangeDTO exchangeDTO = exchangeService.postExchange(1,exchange);
        // 1-2. 교환 삭제
        exchangeService.deleteExchange(1, exchangeDTO.getExchangeCode());
        // 1-3. 교환 생성
        exchangeDTO = exchangeService.postExchange(1,exchange);

        // 2. 발주 등록
        Map<Integer,Integer> requestProducts =  new HashMap<Integer,Integer>(){{ put(1, 1); put(2,2); put(3,3);}};
        RequestOrderVO requestOrderVO = new RequestOrderVO(requestProducts,franchise.getFranchiseCode());

        // 2-1. 발주 생성
        OrderDTO orderDTO = orderFacade.postFranchiseOrder(franchise.getFranchiseCode(),requestOrderVO);

        // 2-2. 발주 수정
        RequestPutOrder requestPutOrder = new RequestPutOrder(orderDTO.getOrderCode(),requestProducts,franchiseCode);
        boolean result = orderService.putFranchiseOrder(franchiseCode,requestPutOrder);
        // 2-3. 발주 거절
        orderFacade.denyOrder(adminCode, orderDTO.getOrderCode(), "안녕 하이 헬로우");
        // 2-4. 발주 수정
        result = orderService.putFranchiseOrder(franchiseCode,requestPutOrder);

        // 2-5 발주 승인
        orderDTO =  orderFacade.acceptOrder(adminCode, orderDTO.getOrderCode());

        Order order = orderRepository.findById(orderDTO.getOrderCode()).orElseThrow();
        Exchange exchange1 = exchangeRepository.findById(exchangeDTO.getExchangeCode()).orElseThrow();
        Invoice invoice = invoiceRepository.findByOrderOrderCode(order.getOrderCode());
        Specs specs = specsRepository.findByOrderOrderCode(order.getOrderCode());

        Assertions.assertEquals(exchange1.getExchangeCode(), order.getExchange().getExchangeCode());
        Assertions.assertEquals(invoice.getOrder().getOrderCode(), order.getOrderCode());
        Assertions.assertEquals(specs.getOrder().getOrderCode(), order.getOrderCode());


    }

}
