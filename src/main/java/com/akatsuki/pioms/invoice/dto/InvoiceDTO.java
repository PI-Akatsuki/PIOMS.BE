package com.akatsuki.pioms.invoice.dto;

import com.akatsuki.pioms.invoice.aggregate.InvoiceEntity;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.order.aggregate.Order;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class InvoiceDTO {
    private int invoiceCode;
    private DELIVERY_STATUS deliveryStatus;
    private LocalDateTime invoiceDate;
    private int invoiceRegionCode;
    private Order order;

    public InvoiceDTO(InvoiceEntity invoice) {
        this.invoiceCode = invoice.getInvoiceCode();
        this.deliveryStatus = invoice.getDeliveryStatus();
        this.invoiceDate = invoice.getInvoiceDate();
        this.invoiceRegionCode = invoice.getInvoiceRegionCode();
        this.order =invoice.getOrder();
    }
}
