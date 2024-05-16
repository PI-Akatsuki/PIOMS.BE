package com.akatsuki.pioms.invoice.dto;

import com.akatsuki.pioms.driver.DeliveryRegionDTO;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.order.dto.OrderDTO;
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
    private int deliveryRegionCode;

    private OrderDTO order;

    public InvoiceDTO(Invoice invoice) {
        System.out.println("invoice = " + invoice);
        this.invoiceCode = invoice.getInvoiceCode();
        this.deliveryStatus = invoice.getDeliveryStatus();
        this.invoiceDate = invoice.getInvoiceDate();
        this.deliveryRegionCode = invoice.getDeliveryRegion();
//        this.deliveryRegionDTO = new DeliveryRegionDTO(invoice.getDeliveryRegion());
        this.order = new OrderDTO(invoice.getOrder());

    }
}
