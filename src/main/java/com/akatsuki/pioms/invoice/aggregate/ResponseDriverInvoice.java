package com.akatsuki.pioms.invoice.aggregate;


import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.order.dto.OrderProductDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseDriverInvoice {

    private int driverCode;
    private int invoiceCode;
    private int orderCode;
    private List<OrderProductDTO> products;
    private String address;
    private DELIVERY_STATUS deliveryStatus;
    private LocalDateTime invoiceDate;

    public ResponseDriverInvoice(int driverCode,InvoiceDTO invoiceDTO) {

        this.driverCode= driverCode;
        this.invoiceCode= invoiceDTO.getInvoiceCode();
        this.orderCode= invoiceDTO.getOrder().getOrderCode();
        this.products= invoiceDTO.getOrder().getOrderProductList();
        this.address= invoiceDTO.getOrder().getFranchiseAddress();
        this.deliveryStatus= invoiceDTO.getDeliveryStatus();
        this.invoiceDate= invoiceDTO.getInvoiceDate();
    }

}
