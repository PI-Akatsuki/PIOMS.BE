package com.akatsuki.pioms.order.vo;

import com.akatsuki.pioms.franchise.etc.DELIVERY_DATE;
import com.akatsuki.pioms.order.entity.OrderEntity;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private int orderCode;
    private Date orderDate;
    private int orderTotalPrice;
    private ORDER_CONDITION orderCondition;
    private String orderReason;
    private boolean orderStatus;

    private int franchiseCode;
    private String franchiseName;
    private int franchiseOwnerCode;
    private String franchiseOwnerName;
    private DELIVERY_DATE franchiseDeliveryDate;

    private int adminCode;
    private String adminName;

    public OrderVO(OrderEntity order) {
        this.orderCode= order.getOrderCode();
        this.orderDate = order.getOrderDate();
        this.orderTotalPrice = order.getOrderTotalPrice();
        this.orderCondition = order.getOrderCondition();
        this.orderReason = order.getOrderReason();
        this.orderStatus = order.isOrderStatus();
        this.franchiseCode = order.getFranchise().getFranchiseCode();
        this.franchiseName = order.getFranchise().getFranchiseName();
        this.franchiseOwnerCode = order.getFranchise().getFranchiseOwner().getFranchiseOwnerCode();
        this.franchiseOwnerName = order.getFranchise().getFranchiseOwner().getFranchiseOwnerName();
        this.franchiseDeliveryDate = order.getFranchise().getFranchiseDeliveryDate();
        this.adminCode = order.getFranchise().getAdmin().getAdminCode();
        this.adminName = order.getFranchise().getAdmin().getAdminName();
    }
}
