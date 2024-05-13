package com.akatsuki.pioms.event;

import com.akatsuki.pioms.order.entity.Order;
import lombok.Getter;

@Getter
public class OrderEvent{

    private int orderId;
    private int franchiseId;
    private Order order;

    public OrderEvent(int orderId, int franchiseId) {
        this.orderId = orderId;
        this.franchiseId = franchiseId;
    }

    public OrderEvent(Order order){
        this.order = order;
    }

}
