package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.order.entity.OrderEntity;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.vo.OrderListVO;
import com.akatsuki.pioms.order.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    OrderRepository orderRepository;
    ApplicationEventPublisher publisher;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ApplicationEventPublisher publisher) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListVO getFranchisesOrderList(int adminId){
        List<OrderEntity> orderList = orderRepository.findAllByFranchiseAdminAdminCode(adminId);
        System.out.println("orderList = " + orderList);
        List<OrderVO> orderVOList = new ArrayList<>();
        orderList.forEach(order-> {
            orderVOList.add(new OrderVO(order));
        });
        return new OrderListVO(orderVOList);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListVO getFranchisesUncheckedOrderList(int adminId){
        List<OrderEntity> orderList = orderRepository.findAllByFranchiseAdminAdminCodeAndOrderCondition(adminId, ORDER_CONDITION.등록전);
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
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        if(order.getOrderCondition() != ORDER_CONDITION.승인대기){
            return "This order is unavailable to accept. This order's condition is '" + order.getOrderCondition().name() +"', not '승인대기'. ";
        }
        order.setOrderCondition(ORDER_CONDITION.승인완료);
        // -> 재고 변경시켜야 함
        orderRepository.save(order);
        try {
//            publisher.publishEvent(new OrderEvent(orderId,order.getFranchise().getFranchiseCode()));
            publisher.publishEvent(new OrderEvent(order));
        }catch (Exception e){
            System.out.println("exception occuered");
        }

        return "This order is accepted";
    }

    @Override
    @Transactional
    public String denyOrder(int orderId){
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        if(order.getOrderCondition() != ORDER_CONDITION.승인대기){
            return "This order is unavailable to accept. This order's condition is '" + order.getOrderCondition().name() +"', not '승인대기'. ";
        }
        order.setOrderCondition(ORDER_CONDITION.승인거부);
        orderRepository.save(order);
        return "This order is denied.";
    }

    public void postFranchiseOrder(OrderVO order){
//        OrderEntity orderEntity = new OrderEntity(order);

    }


}
