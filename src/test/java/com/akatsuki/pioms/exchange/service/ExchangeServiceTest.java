package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.exchange.aggregate.*;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.repository.ExchangeProductRepository;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class ExchangeServiceTest {

    @Mock
    private ExchangeRepository exchangeRepository;

    @Mock
    private ExchangeProductRepository exchangeProductRepository;

    @Mock
    private FranchiseWarehouseService franchiseWarehouseService;

    @Mock
    private OrderService orderService;

    @Mock
    private FranchiseService franchiseService;

    @Mock
    private ProductService productService;

    @Mock
    private GetUserInfo getUserInfo;

    @InjectMocks
    private ExchangeServiceImpl exchangeService;

    private Exchange exchange;
    private ExchangeProduct exchangeProduct;
    private RequestExchange requestExchange;
    private Order order;
    private Admin admin;
    private Franchise franchise;
    private Product product;
    private FranchiseOwner franchiseOwner;
    private DeliveryDriver deliveryDriver;

    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .adminCode(1)
                .adminId("root")
                .accessNumber("rootAccess")
                .adminStatus(true)
                .adminName("root")
                .adminEmail("root@example.com")
                .adminPhone("010-1234-5678")
                .enrollDate("2023-01-01 00:00:00")
                .updateDate("2023-01-01 00:00:00")
                .adminRole("ROLE_ROOT")
                .franchise(new ArrayList<>())
                .build();

        franchiseOwner = FranchiseOwner.builder()
                .franchiseOwnerCode(1)
                .franchiseOwnerName("test")
                .franchiseOwnerId("sadf")
                .franchiseRole("ROLE_OWNER")
                .build();

        deliveryDriver = DeliveryDriver.builder()
                .driverCode(1)
                .driverId("driver")
                .driverName("driver")
                .driverRole("ROLE_DRIVER").build();

        franchise = Franchise.builder()
                .franchiseCode(1)
                .franchiseOwner(franchiseOwner)
                .admin(admin)
                .franchiseName("franchise")
                .franchiseDeliveryDate(DELIVERY_DATE.월_목)
                .deliveryDriver(deliveryDriver)
                .build();

        order = Order.builder()
                .orderCode(1)
                .orderCondition(ORDER_CONDITION.승인대기)
                .franchise(franchise)
                .orderProductList(null)
                .build();
        exchange = new Exchange();
        exchange.setExchangeCode(1);
        exchange.setExchangeDate(LocalDateTime.now());
        exchange.setExchangeStatus(EXCHANGE_STATUS.반송신청);
//        exchange.setFranchise(franchise);

        exchangeProduct = new ExchangeProduct();
        exchangeProduct.setExchangeProductCount(10);

        List<ExchangeProduct> products = new ArrayList<>();
        products.add(exchangeProduct);
        exchange.setProducts(products);

        List<ExchangeProductVO> productVOs = new ArrayList<>();
        ExchangeProductVO productVO = new ExchangeProductVO(1,1,"",10,10,0,EXCHANGE_PRODUCT_STATUS.교환);
        productVOs.add(productVO);
        requestExchange = new RequestExchange(EXCHANGE_STATUS.반송신청,productVOs);
    }

    @Test
    @DisplayName("본사에서 가맹점으로 돌려보내야할 교환 상품을 가진 처리완료 상태의 발주 리스트 조회 ")
    void testFindExchangeToSend() {
        //given
        when(exchangeRepository.findByFranchiseFranchiseCodeAndExchangeStatus(1, EXCHANGE_STATUS.반송신청))
                .thenReturn(exchange);
        when(exchangeRepository.save(any(Exchange.class))).thenReturn(exchange);

        //when
        ExchangeDTO result = exchangeService.findExchangeToSend(1);

        //then
        assertNotNull(result);
        assertEquals(EXCHANGE_STATUS.반송중, result.getExchangeStatus());
        verify(exchangeRepository, times(1))
                .findByFranchiseFranchiseCodeAndExchangeStatus(1, EXCHANGE_STATUS.반송신청);
    }

    @Test
    @DisplayName("루트관리자가 조회할 때")
    void testGetExchangesByAdminCode_RootAdmin() {
        //given
        when(getUserInfo.getAdminCode()).thenReturn(1);
        when(exchangeRepository.findAll()).thenReturn(List.of(exchange));

        //when
        List<ExchangeDTO> result = exchangeService.getExchangesByAdminCode();

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(exchangeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("본사 관리자가 조회할 때")
    void testGetExchangesByAdminCode_NonRootAdmin() {
        //given
        when(getUserInfo.getAdminCode()).thenReturn(2);
        when(exchangeRepository.findAllByFranchiseAdminAdminCodeOrderByExchangeDateDesc(2))
                .thenReturn(List.of(exchange));

        //when
        List<ExchangeDTO> result = exchangeService.getExchangesByAdminCode();

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(exchangeRepository, times(1))
                .findAllByFranchiseAdminAdminCodeOrderByExchangeDateDesc(2);
    }

    @Test
    @DisplayName("반품서 신청")
    void testPostExchange() {
        //given
        when(getUserInfo.getFranchiseOwnerCode()).thenReturn(1);
        when(franchiseService.findFranchiseByFranchiseOwnerCode(1)).thenReturn(new FranchiseDTO(franchise));
        when(exchangeRepository.existsByFranchiseFranchiseCodeAndExchangeStatus(1, EXCHANGE_STATUS.반송신청))
                .thenReturn(false);
        when(franchiseWarehouseService.checkEnableToAddExchangeAndChangeEnableCnt(any(RequestExchange.class), eq(1)))
                .thenReturn(true);
        when(exchangeRepository.save(any(Exchange.class))).thenReturn(exchange);
        when(exchangeProductRepository.save(any(ExchangeProduct.class))).thenReturn(exchangeProduct);
        when(exchangeRepository.findById(anyInt())).thenReturn(Optional.of(exchange));

        //when
        ExchangeDTO result = exchangeService.postExchange(requestExchange);

        //then
        assertNotNull(result);
        assertEquals(EXCHANGE_STATUS.반송신청, result.getExchangeStatus());
        verify(exchangeRepository, times(1)).save(any(Exchange.class));
        verify(exchangeProductRepository, times(1)).save(any(ExchangeProduct.class));
    }

    @Test
    @DisplayName("관리자의 반품 조회")
    void testGetExchangeByAdminCode() {
        //given
        exchange.setFranchise(franchise);
        when(getUserInfo.getAdminCode()).thenReturn(1);
        when(exchangeRepository.findById(1)).thenReturn(Optional.of(exchange));

        //when
        ExchangeDTO result = exchangeService.getExchangeByAdminCode(1);

        //then
        assertNotNull(result);
    }

    @Test
    @DisplayName("점주의 반품 조회")
    void testGetExchangeByFranchiseOwnerCode() {
        //given
        exchange.setFranchise(franchise);
        when(getUserInfo.getFranchiseOwnerCode()).thenReturn(1);
        when(exchangeRepository.findById(1)).thenReturn(Optional.of(exchange));

        //when
        ExchangeDTO result = exchangeService.getExchangeByFranchiseOwnerCode(1);

        //then
        assertNotNull(result);
    }

    @Test
    @DisplayName("가맹점주가 자신의 반품서들을 조회할 때")
    void testGetFrOwnerExchanges() {
        //given
        when(getUserInfo.getFranchiseOwnerCode()).thenReturn(1);
        when(exchangeRepository.findAllByFranchiseFranchiseOwnerFranchiseOwnerCodeOrderByExchangeDateDesc(1))
                .thenReturn(List.of(exchange));

        //when
        List<ExchangeDTO> result = exchangeService.getFrOwnerExchanges();

        //then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("반품서 삭제")
    void testDeleteExchange() {
        //given
        when(getUserInfo.getFranchiseOwnerCode()).thenReturn(1);
        when(exchangeRepository.findById(1)).thenReturn(Optional.of(exchange));
        when(orderService.findOrderByExchangeCode(1)).thenReturn(false);
        exchange.setFranchise(franchise);
        exchange.setProducts(new ArrayList<>());
        //when
        boolean result = exchangeService.deleteExchange(1);

        //then
        assertTrue(result);
        verify(exchangeRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("반환대기중인 상품 처리")
    void testUpdateExchangeStartDelivery() {
        //given
        when(exchangeRepository.findAllByFranchiseFranchiseCodeAndExchangeStatus(1, EXCHANGE_STATUS.반환대기))
                .thenReturn(List.of(exchange));

        //when
        exchangeService.updateExchangeStartDelivery(1);

        //then
        assertEquals(EXCHANGE_STATUS.반환중, exchange.getExchangeStatus());
    }

    @Test
    @DisplayName("배송기사가 배송완료 시 발생될 이벤트")
    void testUpdateExchangeEndDelivery() {
        //given
        when(exchangeRepository.findAllByFranchiseFranchiseCodeAndExchangeStatus(1, EXCHANGE_STATUS.반환중))
                .thenReturn(List.of(exchange));

        //when
        boolean result = exchangeService.updateExchangeEndDelivery(1);

        //then
        assertTrue(result);
        assertEquals(EXCHANGE_STATUS.반환완료, exchange.getExchangeStatus());
        verify(exchangeRepository, times(1)).save(exchange);
    }

    @Test
    @DisplayName("배송완료시 가맹에서 본사로 보낼 반품 처리")
    void testUpdateExchangeToCompany() {
        //given
        exchange.setFranchise(franchise);
        when(exchangeRepository.findById(1)).thenReturn(Optional.of(exchange));

        //when
        exchangeService.updateExchangeToCompany(1);

        //then
        assertEquals(EXCHANGE_STATUS.처리대기, exchange.getExchangeStatus());
        verify(exchangeRepository, times(1)).save(exchange);
    }

    @Test
    @DisplayName("관리자가 가맹으로부터 온 반품 검수")
    void testProcessArrivedExchange() throws JsonProcessingException {
        //given
        Exchange requiredExchange = exchange;
        exchange.setFranchise(franchise);
        requiredExchange.setExchangeStatus(EXCHANGE_STATUS.처리대기);

        System.out.println("requiredExchange = " + requiredExchange);

        when(getUserInfo.getAdminCode()).thenReturn(1);
        when(exchangeRepository.findById(1)).thenReturn(Optional.of(requiredExchange));
//        when(productService.importExchangeProducts(any(RequestExchange.class))).thenReturn(true);
        when(exchangeProductRepository.findById(any(Integer.class))) .thenReturn(Optional.ofNullable(exchangeProduct));
        Exchange wantedResult = exchange;
        when(exchangeRepository.save(any(Exchange.class))).thenReturn(wantedResult);

        //when
        ExchangeDTO result = exchangeService.processArrivedExchange(1, requestExchange);

        //then
        assertNotNull(result);
        assertEquals(EXCHANGE_STATUS.처리완료, result.getExchangeStatus());
        verify(exchangeRepository, times(1)).save(exchange);
    }

    @Test
    @DisplayName("발주 승인 후 발주에 대하여 반환 대기중인 상품들 처리 준비시킴")
    void testAfterAcceptOrder() {
        //given
        OrderDTO order = new OrderDTO();
        order.setFranchiseCode(1);
        when(exchangeRepository.findAllByFranchiseFranchiseCodeAndExchangeStatus(1, EXCHANGE_STATUS.처리완료))
                .thenReturn(List.of(exchange));

        //when
        exchangeService.afterAcceptOrder(order);

        //then
        assertEquals(EXCHANGE_STATUS.반환대기, exchange.getExchangeStatus());
        verify(exchangeRepository, times(1)).save(exchange);
    }
}
