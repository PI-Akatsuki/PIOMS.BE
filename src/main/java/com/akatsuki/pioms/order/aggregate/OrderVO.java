package com.akatsuki.pioms.order.aggregate;


import com.akatsuki.pioms.exchange.dto.ExchangeProductDTO;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.dto.OrderProductDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO {
    private int orderCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    private int orderTotalPrice;

    private ORDER_CONDITION orderCondition;
    private String orderReason;

    private int franchiseCode;
    private String franchiseName;

    private int franchiseOwnerCode;
    private String franchiseOwnerName;
    private String franchiseOwnerPhone;

    private DELIVERY_DATE franchiseDeliveryDate;
    private int adminCode;
    private String adminName;
    private String adminPhone;
    private List<OrderProductDTO> orderProductList  = new ArrayList<>();
    private List<ExchangeProductDTO> exchangeProductList  = new ArrayList<>();

    private int invoiceCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceDate;

    public OrderVO(OrderDTO order) {
        this.orderCode= order.getOrderCode();
        this.orderDate = order.getOrderDate();
        this.orderTotalPrice = order.getOrderTotalPrice();
        this.orderCondition = order.getOrderCondition();

        this.orderReason = order.getOrderReason();
        this.franchiseCode = order.getFranchiseCode();
        this.franchiseName = order.getFranchiseName();

        this.franchiseOwnerCode = order.getFranchiseOwnerCode();
        this.franchiseOwnerName = order.getFranchiseOwnerName();
        this.franchiseOwnerPhone = order.getFranchiseOwnerPhone();
        this.franchiseDeliveryDate = order.getDeliveryDate();


        this.adminCode = order.getAdminCode();
        this.adminName = order.getAdminName();
        this.adminPhone = order.getAdminPhone();
        this.orderProductList = order.getOrderProductList();
        if (order.getExchange()!=null) {
            this.exchangeProductList = order.getExchange().getExchangeProducts();
        }
        if (order.getInvoiceCode()!=0) {
            invoiceCode = order.getInvoiceCode();
            invoiceDate = order.getInvoiceDate();
        }
    }
}
