package com.akatsuki.pioms.order.dto;

import com.akatsuki.pioms.exchange.aggregate.Exchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.OrderProduct;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private int franchiseCode;
    private String franchiseName;
    private DELIVERY_DATE deliveryDate;

    private int franchiseOwnerCode;
    private String franchiseOwnerName;

    private int AdminCode;
    private String AdminName;

    private ExchangeDTO exchange;

    private List<OrderProductDTO> orderProductList;


    public OrderDTO(Order order) {
        this.orderCode= order.getOrderCode();
        this.orderDate= order.getOrderDate();
        this.orderTotalPrice= order.getOrderTotalPrice();
        this.orderCondition= order.getOrderCondition();
        this.orderReason= order.getOrderReason();
        this.orderStatus= order.isOrderStatus();
        this.franchiseCode= order.getFranchise().getFranchiseCode();
        this.franchiseName= order.getFranchise().getFranchiseName();
        this.deliveryDate= order.getFranchise().getFranchiseDeliveryDate();
        this.franchiseOwnerCode= order.getFranchise().getFranchiseOwner().getFranchiseOwnerCode();
        this.franchiseOwnerName= order.getFranchise().getFranchiseOwner().getFranchiseOwnerName();
        this.AdminCode= order.getFranchise().getAdmin().getAdminCode();
        this.AdminName= order.getFranchise().getAdmin().getAdminName();
        if (exchange!=null)
            this.exchange= new ExchangeDTO(order.getExchange());
//        this.orderProductList= order.getOrderProductList();
        orderProductList = new ArrayList<>();
        for (int i = 0; i < order.getOrderProductList().size(); i++) {
            orderProductList.add(new OrderProductDTO(order.getOrderProductList().get(i)));
        }
    }
}
