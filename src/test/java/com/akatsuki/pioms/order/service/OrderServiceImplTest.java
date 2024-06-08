package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.repository.OrderProductRepository;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.product.aggregate.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    void testGetOrderListByAdminCode() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);

        // Mock 설정
        when(orderRepository.findAllDesc()).thenReturn(orderList);

        // Mock 설정 후 출력 확인
        List<Order> mockOrderList = orderRepository.findAllDesc();
        System.out.println("Mock 설정 후 orderRepository.findAllDesc() = " + mockOrderList);

        // 서비스 호출
        List<OrderDTO> orders = orderService.getOrderListByAdminCode(1);

        // 검증
        assertNotNull(orders);
        assertEquals(1, orders.size());
        verify(orderRepository, times(2)).findAllDesc();
    }

    @Test
    void testAcceptOrder() {
        int adminCode = 1;
        int orderCode = 100;
        ExchangeDTO exchangeDTO = new ExchangeDTO();
        exchangeDTO.setExchangeCode(200);

        Order order = new Order();
        when(orderRepository.findById(orderCode)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO result = orderService.acceptOrder(adminCode, orderCode, exchangeDTO);

        assertNotNull(result);
        assertEquals(ORDER_CONDITION.승인완료, order.getOrderCondition());
        assertEquals(200, order.getExchange().getExchangeCode());
    }



}
