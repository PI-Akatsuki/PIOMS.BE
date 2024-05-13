package com.akatsuki.pioms.order.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class OrderListVO {

    List<OrderVO> orderList;

    public OrderListVO(List<Order> franchisesOrderList) {
        orderList = new ArrayList<>();
        franchisesOrderList.forEach(
                order -> {
                    orderList.add(new OrderVO(order));
                }
        );
    }

}
