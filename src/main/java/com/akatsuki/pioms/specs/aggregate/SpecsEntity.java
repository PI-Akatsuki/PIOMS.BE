package com.akatsuki.pioms.specs.aggregate;


import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
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
    private Franchise franchise;

    @JoinColumn(name = "request_code")
    @OneToOne
    private Order order;

    public SpecsEntity(int orderId, int franchiseId) {
        this.specsDate = LocalDateTime.now();
        Order order1 = new Order();
        order1.setOrderCode(orderId);
        this.order= order1;
        Franchise franchise1 = new Franchise();
        franchise1.setFranchiseCode(franchiseId);
        this.franchise = franchise1;
    }

}
