package com.akatsuki.pioms.order.aggregate;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.exchange.aggregate.Exchange;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "request")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Order {

    @Id
    @Column(name = "request_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderCode;

    @Column(name = "request_date")
    private LocalDateTime orderDate;

    @Column(name = "request_total_price")
    private int orderTotalPrice;

    @Column(name = "request_condition")
    @Enumerated(EnumType.STRING)
    private ORDER_CONDITION orderCondition;

    @Column(name = "request_reason")
    private String orderReason;


    @JoinColumn(name = "franchise_code")
    @ManyToOne
    private Franchise franchise;

    @JoinColumn(name = "exchange_code")
    @OneToOne
    private Exchange exchange;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.PERSIST)
    private List<OrderProduct> orderProductList;

    @OneToOne(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Invoice invoice;

    public Order(ORDER_CONDITION orderCondition,FranchiseDTO franchise) {
        this.orderDate = LocalDateTime.now();
        this.orderCondition = orderCondition;
        Franchise franchise1 = new Franchise();
        franchise1.setFranchiseCode(franchise.getFranchiseCode());
        this.franchise = franchise1;
    }


    public Order(OrderDTO orderDTO) {
        this.orderCode= orderDTO.getOrderCode();
        this.orderDate= orderDTO.getOrderDate();
        this.orderTotalPrice= orderDTO.getOrderTotalPrice();
        this.orderCondition= orderDTO.getOrderCondition();
        this.orderReason= orderDTO.getOrderReason();
        Franchise franchise1 = new Franchise();
        franchise1.setFranchiseCode(orderDTO.getFranchiseCode());
        franchise1.setFranchiseName(orderDTO.getFranchiseName());
        FranchiseOwner franchiseOwner = new FranchiseOwner();
        franchiseOwner.setFranchiseOwnerCode(orderDTO.getFranchiseOwnerCode());
        franchiseOwner.setFranchiseOwnerName(orderDTO.getFranchiseOwnerName());
        franchise1.setFranchiseOwner(franchiseOwner);
        franchise1.setAdmin(new Admin());
        franchise1.getAdmin().setAdminCode(orderDTO.getAdminCode());
        franchise1.getAdmin().setAdminName(orderDTO.getAdminName());

        this.franchise= franchise1;
        if (orderDTO.getExchange()!=null) {
            Exchange exchange1 = new Exchange();
            exchange1.setExchangeCode(orderDTO.getExchange().getExchangeCode());
            this.exchange = exchange1;
        }

    }
}
