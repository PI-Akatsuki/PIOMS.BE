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
        ORDER_CONDITION pastOrderCondition = order.getOrderCondition();
        OrderDTO orderDTO1 = orderFacade.acceptOrder(order.getFranchise().getAdmin().getAdminCode(),
                order.getOrderCode());
        assertNotEquals(pastOrderCondition,orderDTO1.getOrderCondition());
        assertEquals(ORDER_CONDITION.승인완료, orderDTO1.getOrderCondition());
    }

    @Test
    void denyOrder() {
        ORDER_CONDITION pastOrderCondition = order.getOrderCondition();
        String denyReason = "당신은 이게 발주라고 보낸건가요?";
        String result = orderFacade.denyOrder(order.getFranchise().getAdmin().getAdminCode(),
                order.getOrderCode(), denyReason );

        assertNotEquals(pastOrderCondition, order.getOrderCondition());
        assertEquals(ORDER_CONDITION.승인거부, order.getOrderCondition());
        assertEquals(denyReason, order.getOrderReason());
    }

    @Test
    void postFranchiseOrder() {
        Franchise franchise = franchiseService.findFranchiseById(2).orElseThrow();
        Map<Integer,Integer> requestProducts =  new HashMap<Integer,Integer>(){{ put(1, 1); put(2,2); put(3,3);}};
        RequestOrderVO requestOrderVO = new RequestOrderVO(requestProducts,franchise.getFranchiseCode());

        OrderDTO orderDTO1 = orderFacade.postFranchiseOrder(franchise.getFranchiseCode(),requestOrderVO);
        if (orderDTO1 == null){
            return;
        }
        Order order1 = orderRepository.findById(orderDTO1.getOrderCode()).orElseThrow();

        assertEquals(order1.getFranchise().getAdmin().getAdminCode(), orderDTO1.getAdminCode());
        System.out.println("order1.getOrderProductList() = " + order1.getOrderProductList());
//        assertEquals(order1.getOrderProductList().size(), orderDTO1.getOrderProductList().size());
        assertEquals(order1.getFranchise().getFranchiseCode(), orderDTO1.getFranchiseCode());
    }

    @Test
    void getOrderListByFranchiseCode() {
        List<Order> orders = orderRepository.findByFranchiseFranchiseCode(franchise.getFranchiseCode());
        List<OrderDTO>orderDTOS = orderService.getOrderList(franchise.getFranchiseCode());
        assertEquals(orders.size(), orderDTOS.size());
    }
}