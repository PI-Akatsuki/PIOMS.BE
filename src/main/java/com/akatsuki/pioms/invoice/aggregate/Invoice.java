package com.akatsuki.pioms.invoice.aggregate;


import com.akatsuki.pioms.order.aggregate.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "invoice")
@Entity
public class Invoice {

    @Id
    @Column(name = "invoice_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invoiceCode;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DELIVERY_STATUS deliveryStatus;

    @Column(name = "delivery_date")
    private LocalDateTime invoiceDate;

    @JoinColumn(name = "request_code")
    @OneToOne
    @ToString.Exclude
    private Order order;

}
