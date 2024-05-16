package com.akatsuki.pioms.invoice.aggregate;

import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
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
    private int deliveryRegionCode;
    private int orderCode;
    private List<ResponseInvoiceOrderProduct> orderProductVOList;

    public ResponseInvoice(InvoiceDTO invoiceDTO) {
        this.invoiceCode = invoiceDTO.getInvoiceCode();
        this.deliveryStatus = invoiceDTO.getDeliveryStatus();
        this.invoiceDate = invoiceDTO.getInvoiceDate();
        this.deliveryRegionCode = invoiceDTO.getDeliveryRegionCode();
        if(invoiceDTO.getOrder()!=null){
            this.orderCode = invoiceDTO.getOrder().getOrderCode();
            this.orderProductVOList = new ArrayList<>();
            invoiceDTO.getOrder().getOrderProductList()
                .forEach(product-> this.orderProductVOList.add(new ResponseInvoiceOrderProduct(product.getProductName(), product.getRequestProductCount())));
        }
    }
}
