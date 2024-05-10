package com.akatsuki.pioms.order.dto;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;

import java.time.LocalDateTime;


public class OrderDTO {
    private int orderCode;
    private LocalDateTime orderDate;
    private int orderTotalPrice;
    private ORDER_CONDITION orderCondition;
    private String orderReason;
    private boolean orderStatus;
    private Franchise franchise;
}
