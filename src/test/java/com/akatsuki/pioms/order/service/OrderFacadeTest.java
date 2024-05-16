package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
class OrderFacadeTest {
    private OrderFacade orderFacade;
    private OrderRepository orderRepository;
    private OrderService orderService;
    private FranchiseService franchiseService;

    @Autowired
    public OrderFacadeTest(OrderFacade orderFacade, OrderRepository orderRepository, OrderService orderService, FranchiseService franchiseService) {
        this.orderFacade = orderFacade;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.franchiseService = franchiseService;
    }

    static Order order;
    static Franchise franchise;
    static OrderDTO orderDTO;
    @BeforeEach
    void init(){
        franchise = franchiseService.findFranchiseById(1).orElseThrow();
        Map<Integer,Integer> requestProducts =  new HashMap<Integer,Integer>(){{ put(1, 1); put(2,2); put(3,3);}};

        RequestOrderVO requestOrderVO = new RequestOrderVO(requestProducts,franchise.getFranchiseCode());
        orderDTO = orderFacade.postFranchiseOrder(franchise.getFranchiseCode(),requestOrderVO);
        order = orderRepository.findById(orderDTO.getOrderCode()).orElseThrow();
    }

    @Test
    void getOrderListByAdminCode() {
        List<Order> orders = orderRepository.findAllByFranchiseAdminAdminCode(order.getFranchise().getAdmin().getAdminCode());
        List<OrderDTO> orderDTOS = orderFacade.getOrderListByAdminCode(order.getFranchise().getAdmin().getAdminCode());
        assertEquals(orders.size(), orderDTOS.size());
    }

    @Test
    void getAdminUncheckesOrders() {
        List<Order> orders = orderRepository.findAllByFranchiseAdminAdminCodeAndOrderCondition(order.getFranchise().getAdmin().getAdminCode(), ORDER_CONDITION.승인대기);
        List<OrderDTO> orderDTOS = orderFacade.getAdminUncheckesOrders(order.getFranchise().getAdmin().getAdminCode());
        assertEquals(orders.size(), orderDTOS.size());
        for (int i = 0; i < orderDTOS.size(); i++) {
            assertEquals(orderDTOS.get(i).getOrderCondition(), ORDER_CONDITION.승인대기);
        }
    }

    @Test
    void getAdminOrder() {
        Order order1 = orderRepository.findById(order.getOrderCode()).orElseThrow();
        OrderDTO orderDTO1 = orderFacade.getAdminOrder(order.getFranchise().getAdmin().getAdminCode(), order.getOrderCode());
        assertEquals(order1.getOrderCode(), orderDTO1.getOrderCode());

    }

    @Test
    void acceptOrder() {
    }

    @Test
    void denyOrder() {
    }

    @Test
    void postFranchiseOrder() {
    }

    @Test
    void getOrderListByFranchiseCode() {
    }
}