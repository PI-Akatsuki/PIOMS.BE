package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.OrderProduct;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.aggregate.RequestPutOrder;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.repository.OrderProductRepository;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.product.aggregate.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderProductRepository orderProductRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

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
    }

    @Test
    @DisplayName("관리자가 관리하는 발주 목록  조회")
    void testGetOrderListByAdminCode() {
        //given
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        when(orderRepository.findAllDesc()).thenReturn(orderList);
        List<Order> mockOrderList = orderRepository.findAllDesc();
        System.out.println("Mock 설정 후 orderRepository.findAllDesc() = " + mockOrderList);
        //when
        List<OrderDTO> orders = orderService.getOrderListByAdminCode(1);
        //then
        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(2)).findAllDesc();
    }

    @Test
    @DisplayName("발주 승인 성공 시")
    void testAcceptOrder_Success() {
        //given
        int adminCode = 1;
        int orderCode = 100;
        ExchangeDTO exchangeDTO = new ExchangeDTO();
        exchangeDTO.setExchangeCode(200);

        when(orderRepository.findById(orderCode)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        //when
        OrderDTO result = orderService.acceptOrder(adminCode, orderCode, exchangeDTO);
        //then
        assertNotNull(result);
        assertEquals(ORDER_CONDITION.승인완료, order.getOrderCondition());
        assertEquals(200, order.getExchange().getExchangeCode());
    }

    @Test
    @DisplayName("발주 승인 실패 시")
    void testAcceptOrder_OrderNotFound() {
        //given
        int adminCode = 1;
        int orderCode = 100;
        ExchangeDTO exchangeDTO = new ExchangeDTO();
        exchangeDTO.setExchangeCode(200);
        when(orderRepository.findById(orderCode)).thenReturn(Optional.empty());
        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                orderService.acceptOrder(adminCode, orderCode, exchangeDTO));
        //then
        assertEquals("Order not found with code: 100", exception.getMessage());
    }


    @Test
    @DisplayName("발주 거절")
    void testDenyOrder() {
        //given
        int adminCode = 1;
        int orderCode = 100;
        ExchangeDTO exchangeDTO = new ExchangeDTO();
        exchangeDTO.setExchangeCode(200);

        when(orderRepository.findById(orderCode)).thenReturn(Optional.of(order));
        //when
        int result = orderService.denyOrder(adminCode, orderCode, "denied");
        //then
        assertNotNull(result);
    }


    @Test
    @DisplayName("발주 생성")
    void postOrder(){
        //given
        Map<Integer,Integer> map = new HashMap<>();
        map.put(1,1);
        RequestOrderVO requestOrderVO = new RequestOrderVO(map,1,0);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderProductRepository.save(any(OrderProduct.class))).thenReturn(new OrderProduct());
        //when
        int result = orderService.postFranchiseOrder(new FranchiseDTO(franchise),requestOrderVO,0);
        //then
        assertEquals(1,result);
    }
    //given
    //when
    //then
    @Test
    @DisplayName("점주가 관리하는 발주 조회")
    void getOrderByFranchiseOwnerCode(){
        //given
        when(orderRepository.findById(1)).thenReturn(Optional.ofNullable(order));
        //when
        OrderDTO orderDTO = orderService.getOrder(1,1);
        //then
        assertNotNull(orderDTO);
    }

    @Test
    @DisplayName("발주 수정")
    void putFranchiseOrder(){
        //given
        Map<Integer,Integer> map = new HashMap<>();
        map.put(1,1);
        RequestPutOrder requestOrderVO = new RequestPutOrder(1, map);
        when(orderRepository.findById(1)).thenReturn(Optional.ofNullable(order));
        when(orderProductRepository.save(any(OrderProduct.class))).thenReturn(new OrderProduct());
        //when
        boolean result = orderService.putFranchiseOrder(1,requestOrderVO,0);
        //then
        assertTrue(result);
    }


}
