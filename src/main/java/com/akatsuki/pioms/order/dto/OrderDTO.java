package com.akatsuki.pioms.order.dto;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.OrderProduct;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private int orderCode;
    private LocalDateTime orderDate;
    private int orderTotalPrice;
    private ORDER_CONDITION orderCondition;
    private String orderReason;
    private boolean orderStatus;
    private FranchiseDTO franchise;
    private ExchangeDTO exchange;
    private List<OrderProductDTO> OrderProductList;

    public OrderDTO(Order order) {
        this.orderCode = order.getOrderCode();
        this.orderDate =order.getOrderDate();
        this.orderTotalPrice = order.getOrderTotalPrice();
        this.orderCondition  = order.getOrderCondition();
        this.orderReason = order.getOrderReason();
        this.orderStatus = order.isOrderStatus();
        this.franchise = new FranchiseDTO(order.getFranchise());
        this.exchange = new ExchangeDTO(order.getExchange());

    }
}
