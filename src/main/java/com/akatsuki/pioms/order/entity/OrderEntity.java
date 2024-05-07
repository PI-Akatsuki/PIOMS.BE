package com.akatsuki.pioms.order.entity;

import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.franchise.entity.FranchiseEntity;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "request")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderEntity {

    @Id
    @Column(name = "request_code")
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
    private FranchiseEntity franchise;

    @JoinColumn(name = "exchange_code")
    @OneToOne
    private ExchangeEntity exchange;

}
