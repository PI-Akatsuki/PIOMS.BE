package com.akatsuki.pioms.order.aggregate;

import com.akatsuki.pioms.exchange.aggregate.Exchange;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
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

    @Column(name = "request_status")
    private boolean orderStatus;

    @JoinColumn(name = "franchise_code")
    @ManyToOne
    private Franchise franchise;

    @JoinColumn(name = "exchange_code")
    @OneToOne
    private Exchange exchange;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProductList;

    public Order(ORDER_CONDITION orderCondition, boolean orderStatus, Franchise franchise) {
        this.orderDate = LocalDateTime.now();
        this.orderCondition = orderCondition;
        this.orderStatus = orderStatus;
        this.franchise = franchise;
    }

    public Order(OrderDTO order) {
        this.orderCode = order.getOrderCode();
        this.orderDate = order.getOrderDate();
        this.orderTotalPrice = order.getOrderTotalPrice();
        this.orderCondition = order.getOrderCondition();
        this.orderReason = order.getOrderReason();
        this.orderStatus = order.isOrderStatus();
        this.franchise = new Franchise(order.getFranchise());
        this.exchange = new Exchange(order.getExchange());
        this.orderProductList = new ArrayList<>();
        for (int i = 0; i < order.getOrderProductList().size(); i++) {
            orderProductList.add(new OrderProduct(order.getOrderProductList().get(i)));
        }
    }
}
