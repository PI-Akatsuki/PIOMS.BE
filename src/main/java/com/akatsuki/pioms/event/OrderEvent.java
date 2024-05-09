package com.akatsuki.pioms.event;

import com.akatsuki.pioms.order.entity.OrderEntity;
import lombok.Getter;

@Getter
public class OrderEvent{

    private int orderId;
    private int franchiseId;
    private OrderEntity order;

    public OrderEvent(int orderId, int franchiseId) {
        this.orderId = orderId;
        this.franchiseId = franchiseId;
    }

    public OrderEvent(OrderEntity order){
        this.order = order;
    }

}
