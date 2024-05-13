package com.akatsuki.pioms.event;

import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import lombok.Getter;

@Getter
public class OrderEvent{

    private int orderId;
    private int franchiseId;
    private OrderDTO order;

    public OrderEvent(int orderId, int franchiseId) {
        this.orderId = orderId;
        this.franchiseId = franchiseId;
    }

    public OrderEvent(OrderDTO order){
        this.order = order;
    }

}
