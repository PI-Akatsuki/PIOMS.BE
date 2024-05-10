package com.akatsuki.pioms.specs.aggregate;

import com.akatsuki.pioms.franchise.entity.FranchiseEntity;
import com.akatsuki.pioms.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "specs")
@Entity
public class SpecsEntity {

    @Id
    @Column(name = "specs_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int specsCode;

    @Column(name = "specs_date")
    private LocalDateTime specsDate;

    @JoinColumn(name = "franchise_code")
    @ManyToOne
    private FranchiseEntity franchise;

    @JoinColumn(name = "request_code")
    @OneToOne
    private Order order;

    public SpecsEntity(int orderId, int franchiseId) {
        this.specsDate = LocalDateTime.now();
        this.order.setOrderCode(orderId);
        this.franchise.setFranchiseCode(franchiseId);
    }

    public SpecsEntity(Order orderEntity) {
        this.specsDate = LocalDateTime.now();
        this.order=orderEntity;
        this.franchise = orderEntity.getFranchise();
    }
}
