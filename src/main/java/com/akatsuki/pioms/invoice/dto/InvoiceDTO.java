package com.akatsuki.pioms.invoice.dto;

import com.akatsuki.pioms.driver.DeliveryRegionDTO;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.invoice.aggregate.InvoiceEntity;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private int invoiceCode;
    private DELIVERY_STATUS deliveryStatus;
    private LocalDateTime invoiceDate;
    private DeliveryRegionDTO deliveryRegionDTO;
    private OrderDTO order;

    public InvoiceDTO(InvoiceEntity invoice) {
        this.invoiceCode = invoice.getInvoiceCode();
        this.deliveryStatus = invoice.getDeliveryStatus();
        this.invoiceDate = invoice.getInvoiceDate();
        this.deliveryRegionDTO = new DeliveryRegionDTO(invoice.getDeliveryRegion());
        this.order = new OrderDTO(invoice.getOrder());
    }
}
