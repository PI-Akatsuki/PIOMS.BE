package com.akatsuki.pioms.order.entity;

import com.akatsuki.pioms.franchise.entity.FranchiseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "request")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class OrderEntity {

    @Id
    @Column(name = "order_code")
    private int orderCode;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "order_total_price")
    private int orderTotalPrice;

    @Column(name = "order_condition")
    private boolean orderCondition;

    @Column(name = "order_reason")
    private String orderReason;

    @Column(name = "order_status")
    private boolean orderStatus;

    @JoinColumn(name = "franchise_code")
    @OneToOne
    private FranchiseEntity franchise;

}
