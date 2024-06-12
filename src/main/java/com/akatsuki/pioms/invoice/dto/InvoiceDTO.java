package com.akatsuki.pioms.invoice.dto;

import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceDate;
    private int deliveryRegionCode;

    private OrderDTO order;

    public InvoiceDTO(Invoice invoice) {
        this.invoiceCode = invoice.getInvoiceCode();
        this.deliveryStatus = invoice.getDeliveryStatus();
        this.invoiceDate = invoice.getInvoiceDate();
        this.order = new OrderDTO(invoice.getOrder());
    }
}
