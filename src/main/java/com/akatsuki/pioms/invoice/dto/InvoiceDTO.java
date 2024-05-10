package com.akatsuki.pioms.invoice.dto;

import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.order.dto.OrderDTO;
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
    private OrderDTO order;
}
