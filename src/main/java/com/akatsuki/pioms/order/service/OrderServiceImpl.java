package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.order.entity.OrderEntity;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.vo.OrderListVO;
import com.akatsuki.pioms.order.vo.OrderVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderListVO getFranchisesOrderList(int adminId){
        List<OrderEntity> orderList = orderRepository.findAllByFranchiseAdminAdminCode(adminId);
        System.out.println("orderList = " + orderList);
        List<OrderVO> orderVOList = new ArrayList<>();
        orderList.forEach(order-> {
            orderVOList.add(new OrderVO(order));
        });
        return new OrderListVO(orderVOList);
    }

    public void postFranchiseOrder(OrderVO order){
//        OrderEntity orderEntity = new OrderEntity(order);

    }


}
