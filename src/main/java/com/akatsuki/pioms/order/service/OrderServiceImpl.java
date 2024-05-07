package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.entity.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.entity.FranchiseEntity;
import com.akatsuki.pioms.order.entity.OrderEntity;
import com.akatsuki.pioms.order.entity.OrderProductEntity;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.repository.OrderProductRepository;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.vo.OrderListVO;
import com.akatsuki.pioms.order.vo.OrderVO;
import com.akatsuki.pioms.order.vo.RequestOrderVO;
import com.akatsuki.pioms.product.entity.ProductEntity;
import com.akatsuki.pioms.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    OrderRepository orderRepository;
    ApplicationEventPublisher publisher;
    ExchangeService exchangeService;
    OrderProductRepository orderProductRepository;
    ProductService productService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ApplicationEventPublisher publisher, ExchangeService exchangeService, OrderProductRepository orderProductRepository,ProductService productService) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
        this.exchangeService = exchangeService;
        this.orderProductRepository = orderProductRepository;
        this.productService = productService;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListVO getFranchisesOrderList(int adminCode){
        List<OrderEntity> orderList = orderRepository.findAllByFranchiseAdminAdminCode(adminCode);
        System.out.println("orderList = " + orderList);
        List<OrderVO> orderVOList = new ArrayList<>();
        orderList.forEach(order-> {
            orderVOList.add(new OrderVO(order));
        });
        return new OrderListVO(orderVOList);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListVO getFranchisesUncheckedOrderList(int adminCode){
        List<OrderEntity> orderList = orderRepository.findAllByFranchiseAdminAdminCodeAndOrderCondition(adminCode, ORDER_CONDITION.승인대기);
        System.out.println("orderList = " + orderList);
        List<OrderVO> orderVOList = new ArrayList<>();
        orderList.forEach(order-> {
            orderVOList.add(new OrderVO(order));
        });
        return new OrderListVO(orderVOList);
    }

    @Override
    @Transactional(readOnly = false)
    public String acceptOrder(int orderId) {
        // 주문 찾기
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        if (!checkOrderCondition(order))
            return "This order is unavailable to accept. This order's condition is '" + order.getOrderCondition().name() + "', not '승인대기'. ";

        try {
            order.setOrderCondition(ORDER_CONDITION.승인완료);

            findExchange(order);
            orderRepository.save(order);
            publisher.publishEvent(new OrderEvent(order));
        }catch (Exception e){
            System.out.println("exception occuered");
        }

        return "This order is accepted";
    }

    @Override
    @Transactional
    public String denyOrder(int orderId, String denyMessage){
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        if (!checkOrderCondition(order))
            return "This order is unavailable to accept. This order's condition is '" + order.getOrderCondition().name() + "', not '승인대기'. ";;
        order.setOrderCondition(ORDER_CONDITION.승인거부);
        order.setOrderReason(denyMessage);
        orderRepository.save(order);
        return "This order is denied.";
    }

    @Override
    @Transactional
    public void postFranchiseOrder(RequestOrderVO requestorder){
        OrderEntity order = new OrderEntity();
        order.setOrderDate(LocalDateTime.now());
        order.setOrderCondition(ORDER_CONDITION.승인대기);
        order.setOrderStatus(false);
        FranchiseEntity franchise = new FranchiseEntity();
        franchise.setFranchiseCode(requestorder.getFranchiseCode());
        order.setFranchise(franchise);
        order= orderRepository.save(order);

        int orderId = order.getOrderCode();
        requestorder.getProducts().forEach((productId, count)->{
            OrderEntity order1 = orderRepository.findById(orderId).orElseThrow();
            ProductEntity product = productService.getProduct(productId);
            orderProductRepository.save(new OrderProductEntity(count,0, order1, product));
        });
    }



    private static boolean checkOrderCondition(OrderEntity order) {
        if(order.getOrderCondition() != ORDER_CONDITION.승인대기){
            return false;
        }
        return true;
    }

    private void findExchange(OrderEntity order) {
        // 반품신청 중인 요소 탐색
        ExchangeDTO exchange =  exchangeService.findExchangeToSend(order.getFranchise().getFranchiseCode());

        if(exchange!=null) {
            order.setExchange(new ExchangeEntity(exchange));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListVO getOrderList(int franchiseCode){
        List<OrderEntity> orderList= orderRepository.findByFranchiseFranchiseCode(franchiseCode);
        List<OrderVO> orderVOList = new ArrayList<>();
        orderList.forEach(order-> {
            orderVOList.add(new OrderVO(order));
        });
        return new OrderListVO(orderVOList);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderVO getOrder(int orderCode){
        OrderEntity order = orderRepository.findById(orderCode).orElseThrow();
        System.out.println("order = " + order);
        System.out.println(order.getOrderProductList());
        return new OrderVO(order);
    }
}
