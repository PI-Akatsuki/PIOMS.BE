package com.akatsuki.pioms.order.aggregate;


import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private int orderCode;
    private LocalDateTime orderDate;
    private int orderTotalPrice;
    private ORDER_CONDITION orderCondition;
    private String orderReason;

    private int franchiseCode;
    private String franchiseName;
    private int franchiseOwnerCode;
    private String franchiseOwnerName;
    private DELIVERY_DATE franchiseDeliveryDate;

    private int adminCode;
    private String adminName;

    public OrderVO(OrderDTO order) {
        this.orderCode= order.getOrderCode();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = order.getOrderDate().format(formatter);
        this.orderDate = LocalDateTime.parse(formattedDateTime, formatter);

        this.orderTotalPrice = order.getOrderTotalPrice();
        this.orderCondition = order.getOrderCondition();

        this.orderReason = order.getOrderReason();
        this.franchiseCode = order.getFranchiseCode();
        this.franchiseName = order.getFranchiseName();
        this.franchiseOwnerCode = order.getFranchiseOwnerCode();
        this.franchiseOwnerName = order.getFranchiseOwnerName();
        this.franchiseDeliveryDate = order.getDeliveryDate();
        this.adminCode = order.getAdminCode();
        this.adminName = order.getAdminName();
    }
}
