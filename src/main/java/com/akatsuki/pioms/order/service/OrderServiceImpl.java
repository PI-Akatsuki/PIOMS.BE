package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.entity.OrderEntity;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.vo.OrderListVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderListVO getFranchisesOrderList(int adminId){
        List<OrderEntity> orderList = orderRepository.findAllByFranchiseCodeAdminCodeOrderByOrderDateDesc();
        return null;
    }


}
