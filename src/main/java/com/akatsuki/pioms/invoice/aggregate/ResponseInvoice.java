package com.akatsuki.pioms.invoice.aggregate;

import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseInvoice {
    private int invoiceCode;
    private DELIVERY_STATUS deliveryStatus;
    private LocalDateTime invoiceDate;
    private int invoiceRegionCode;
    private int orderCode;
    private List<OrderProductVO> orderProductVOList;

    public ResponseInvoice(InvoiceDTO invoiceDTO) {
        this.invoiceCode = invoiceDTO.getInvoiceCode();
        this.deliveryStatus = invoiceDTO.getDeliveryStatus();
        this.invoiceDate = invoiceDTO.getInvoiceDate();
        this.invoiceRegionCode = invoiceDTO.getInvoiceRegionCode();
        if(invoiceDTO.getOrder()!=null){
            this.orderCode = invoiceDTO.getOrder().getOrderCode();
            this.orderProductVOList = new ArrayList<>();
            invoiceDTO.getOrder().getOrderProductList()
                .forEach(product-> this.orderProductVOList.add(new OrderProductVO(product.getProduct().getProductName(), product.getRequestProductCount())));
        }
    }
}
