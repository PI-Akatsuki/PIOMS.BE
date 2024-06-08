//package com.akatsuki.pioms;
//
//import com.akatsuki.pioms.exchange.aggregate.*;
//import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
//import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
//import com.akatsuki.pioms.exchange.service.ExchangeService;
//import com.akatsuki.pioms.franchise.aggregate.Franchise;
//import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
//import com.akatsuki.pioms.franchise.service.FranchiseService;
//import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
//import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
//import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
//import com.akatsuki.pioms.invoice.aggregate.Invoice;
//import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
//import com.akatsuki.pioms.invoice.service.InvoiceService;
//import com.akatsuki.pioms.order.aggregate.Order;
//import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
//import com.akatsuki.pioms.order.aggregate.RequestPutOrder;
//import com.akatsuki.pioms.order.aggregate.RequestPutOrderCheck;
//import com.akatsuki.pioms.order.dto.OrderDTO;
//import com.akatsuki.pioms.order.repository.OrderRepository;
//import com.akatsuki.pioms.order.service.AdminOrderFacade;
//import com.akatsuki.pioms.order.service.FranchiseOrderFacade;
//import com.akatsuki.pioms.order.service.OrderService;
//import com.akatsuki.pioms.specs.aggregate.Specs;
//import com.akatsuki.pioms.specs.repository.SpecsRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class ordertest {
//
//    @Autowired
//    AdminOrderFacade orderFacade;
//    @Autowired
//    FranchiseOrderFacade franchiseOrderFacade;
//    @Autowired
//    ExchangeService exchangeService;
//    @Autowired
//    OrderService orderService;
//    @Autowired
//    FranchiseService franchiseService;
//    @Autowired
//    FranchiseWarehouseService franchiseWarehouseService;
//    @Autowired
//    InvoiceService invoiceService;
//    @Autowired
//    OrderRepository orderRepository;
//    @Autowired
//    InvoiceRepository invoiceRepository;
//    @Autowired
//    ExchangeRepository exchangeRepository;
//    @Autowired
//    SpecsRepository specsRepository;
//
//    static List<ExchangeProductVO> exchangeProductVOS;
//    static RequestExchange exchange;
//
//    @Test
//    void testExchangeAndOrderProcesses() {
//        // 1. 초기 설정
//        int adminCode = 1;
//        int franchiseCode = 1;
//        FranchiseDTO franchise = franchiseService.findFranchiseById(franchiseCode).orElseThrow();
//
//        List<ExchangeProductVO> exchangeProductVOS = Stream.of(
//                new ExchangeProductVO(1, 1, EXCHANGE_PRODUCT_STATUS.교환),
//                new ExchangeProductVO(2, 1, EXCHANGE_PRODUCT_STATUS.폐기),
//                new ExchangeProductVO(3, 1, EXCHANGE_PRODUCT_STATUS.교환),
//                new ExchangeProductVO(4, 1, EXCHANGE_PRODUCT_STATUS.폐기)
//        ).toList();
//
//        RequestExchange exchangeRequest = new RequestExchange(1, EXCHANGE_STATUS.반송신청, exchangeProductVOS);
//
//        // 1-1. 교환 생성
//        System.out.println("교환 생성 전 창고: " + franchiseWarehouseService.getFrWarehouseList(franchiseCode));
//        ExchangeDTO exchangeDTO = exchangeService.postExchange(franchiseCode, exchangeRequest);
//        System.out.println("교환 생성 후 창고: " + franchiseWarehouseService.getFrWarehouseList(franchiseCode));
//
//        // 1-2. 교환 삭제
//        exchangeService.deleteExchange(franchiseCode, exchangeDTO.getExchangeCode());
//        System.out.println("교환 삭제 후 창고: " + franchiseWarehouseService.getFrWarehouseList(franchiseCode));
//
//        // 1-3. 교환 재생성
//        exchangeDTO = exchangeService.postExchange(franchiseCode, exchangeRequest);
//        System.out.println("교환 재생성 후 창고: " + franchiseWarehouseService.getFrWarehouseList(franchiseCode));
//
//        // 2. 발주 등록
//        Map<Integer, Integer> requestProducts = Map.of(1, 1, 2, 2, 3, 3);
//        RequestOrderVO requestOrderVO = new RequestOrderVO(requestProducts, franchiseCode);
//
//        // 2-1. 발주 생성
//        System.out.println("requestOrderVO = " + requestOrderVO);
//        OrderDTO orderDTO = franchiseOrderFacade.postFranchiseOrder(franchiseCode, requestOrderVO);
//        Order order = orderRepository.findById(orderDTO.getOrderCode()).orElseThrow();
//
//        // 2-2. 발주 수정
//        RequestPutOrder requestPutOrder = new RequestPutOrder(orderDTO.getOrderCode(), requestProducts, franchiseCode);
//        boolean result = orderService.putFranchiseOrder(franchiseCode, requestPutOrder);
//
//        // 2-3. 발주 거절
//        orderFacade.denyOrder(adminCode, orderDTO.getOrderCode(), "안녕 하이 헬로우");
//
//        // 2-4. 발주 수정
//        result = orderService.putFranchiseOrder(franchiseCode, requestPutOrder);
//        orderDTO = orderFacade.getAdminOrder(adminCode, orderDTO.getOrderCode());
//
//        // 2-5. 발주 승인
//        orderDTO = orderFacade.acceptOrder(adminCode, orderDTO.getOrderCode());
//        order = orderRepository.findById(orderDTO.getOrderCode()).orElseThrow();
//
//        // 검증
//        Exchange exchange = exchangeRepository.findById(exchangeDTO.getExchangeCode()).orElseThrow();
//        Invoice invoice = invoiceRepository.findByOrderOrderCode(order.getOrderCode());
//        Specs specs = specsRepository.findByOrderOrderCode(order.getOrderCode());
//
//        System.out.println("발주 승인 후 order = " + order);
//
//        assertEquals(exchange.getExchangeCode(), order.getExchange().getExchangeCode());
//        assertEquals(invoice.getOrder().getOrderCode(), order.getOrderCode());
//        assertEquals(specs.getOrder().getOrderCode(), order.getOrderCode());
//
//        // 배송 상태 업데이트
//        RequestPutOrderCheck requestPutOrderCheck = new RequestPutOrderCheck(order.getOrderCode(), requestProducts);
//        invoiceService.putInvoice(adminCode, invoice.getInvoiceCode(), DELIVERY_STATUS.배송완료);
//        System.out.println("requestPutOrderCheck = " + requestPutOrderCheck);
//
//        // 검수 전/후 창고 상태 확인
//        List<FranchiseWarehouseDTO> warehouseBeforeCheck = franchiseWarehouseService.getFrWarehouseList(franchiseCode);
//        System.out.println("검수 전 창고 = " + warehouseBeforeCheck);
//        System.out.println(franchiseOrderFacade.putFranchiseOrderCheck(franchiseCode, requestPutOrderCheck));
//        List<FranchiseWarehouseDTO> warehouseAfterCheck = franchiseWarehouseService.getFrWarehouseList(franchiseCode);
//        System.out.println("검수 후 창고 = " + warehouseAfterCheck);
//
//        assertNotEquals(warehouseBeforeCheck, warehouseAfterCheck);
//
//        // 교환 처리 관련 코드 (추가 필요 시)
//        exchangeDTO = exchangeService.getAdminExchange(adminCode,exchangeDTO.getExchangeCode());
//
////        System.out.println("exchangeDTO = " + exchangeDTO);
////        RequestExchange exchangeForProcess = new RequestExchange(EXCHANGE_STATUS.처리완료,exchangeDTO);
////        exchangeDTO =exchangeService.putExchange(adminCode,exchangeDTO.getExchangeCode(),exchangeForProcess);
////        assertEquals(exchangeDTO.getExchangeStatus(),EXCHANGE_STATUS.처리완료);
//
//    }
//
//
//
//}