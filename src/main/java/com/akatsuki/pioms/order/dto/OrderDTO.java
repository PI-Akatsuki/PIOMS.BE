package com.akatsuki.pioms.order.dto;

import com.akatsuki.pioms.exchange.aggregate.Exchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.OrderProduct;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {

    private int orderCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    private int orderTotalPrice;
    private ORDER_CONDITION orderCondition;
    private String orderReason;


    private int franchiseCode;
    private String franchiseName;
    private DELIVERY_DATE deliveryDate;

    private int franchiseOwnerCode;
    private String franchiseOwnerName;
    private String franchiseAddress;
    private String franchiseOwnerPhone;

    private int AdminCode;
    private String AdminName;
    private String adminPhone;
    private ExchangeDTO exchange;

    private List<OrderProductDTO> orderProductList;

    private int invoiceCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceDate;


    public OrderDTO(Order order) {
        this.orderCode= order.getOrderCode();
        this.orderDate= order.getOrderDate();
        this.orderTotalPrice= order.getOrderTotalPrice();
        this.orderCondition= order.getOrderCondition();
        this.orderReason= order.getOrderReason();
        this.franchiseCode= order.getFranchise().getFranchiseCode();
        this.franchiseName= order.getFranchise().getFranchiseName();
        this.deliveryDate= order.getFranchise().getFranchiseDeliveryDate();
        this.franchiseOwnerCode = order.getFranchise().getFranchiseOwner().getFranchiseOwnerCode();
        this.franchiseOwnerName = order.getFranchise().getFranchiseOwner().getFranchiseOwnerName();
        this.franchiseAddress = order.getFranchise().getFranchiseAddress();
        this.franchiseOwnerPhone = order.getFranchise().getFranchiseOwner().getFranchiseOwnerPhone();
        this.AdminCode= order.getFranchise().getAdmin().getAdminCode();
        this.AdminName= order.getFranchise().getAdmin().getAdminName();
        this.adminPhone = order.getFranchise().getAdmin().getAdminPhone();
        if (order.getExchange()!=null) {
            this.exchange = new ExchangeDTO(order.getExchange());
        }
        orderProductList = new ArrayList<>();
        if(order.getOrderProductList()!=null)
            for (int i = 0; i < order.getOrderProductList().size(); i++) {
                orderProductList.add(new OrderProductDTO(order.getOrderProductList().get(i)));
            }

        if (order.getInvoice()!=null){
            invoiceCode = order.getInvoice().getInvoiceCode();
            invoiceDate = order.getInvoice().getInvoiceDate();
        }


    }

    public OrderDTO(Order order, FranchiseDTO franchiseDTO) {
        this.orderCode= order.getOrderCode();
        this.orderDate= order.getOrderDate();
        this.orderTotalPrice= order.getOrderTotalPrice();
        this.orderCondition= order.getOrderCondition();
        this.orderReason= order.getOrderReason();

        this.franchiseCode= franchiseDTO.getFranchiseCode();
        this.franchiseName= franchiseDTO.getFranchiseName();
        this.deliveryDate= franchiseDTO.getFranchiseDeliveryDate();
        this.franchiseOwnerCode = franchiseDTO.getFranchiseOwner().getFranchiseOwnerCode();
        this.franchiseOwnerName = franchiseDTO.getFranchiseOwner().getFranchiseOwnerName();
        this.franchiseAddress = franchiseDTO.getFranchiseAddress();
        this.franchiseOwnerPhone = franchiseDTO.getFranchiseOwner().getFranchiseOwnerPhone();

        this.AdminCode= franchiseDTO.getAdminCode();
        this.AdminName= franchiseDTO.getAdminName();

        if (order.getExchange()!=null) {
            this.exchange = new ExchangeDTO(order.getExchange());
        }
        orderProductList = new ArrayList<>();
        if(order.getOrderProductList()!=null)
            for (int i = 0; i < order.getOrderProductList().size(); i++) {
                orderProductList.add(new OrderProductDTO(order.getOrderProductList().get(i)));
            }

        if (order.getInvoice()!=null){
            invoiceCode = order.getInvoice().getInvoiceCode();
            invoiceDate = order.getInvoice().getInvoiceDate();
        }
    }
}
