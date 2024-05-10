package com.akatsuki.pioms.invoice.aggregate;


import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
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
public class InvoiceEntity {

    @Id
    @Column(name = "invoice_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int invoiceCode;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DELIVERY_STATUS deliveryStatus;

    @Column(name = "delivery_date")
    private LocalDateTime invoiceDate;

    @Column(name = "delivery_region_code")
    private int invoiceRegionCode;

    @JoinColumn(name = "request_code")
    @OneToOne
    private Order order;

}
