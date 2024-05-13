package com.akatsuki.pioms.invoice.vo;

import com.akatsuki.pioms.invoice.entity.InvoiceEntity;
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

    public ResponseInvoice(InvoiceEntity invoiceEntity) {
        this.invoiceCode = invoiceEntity.getInvoiceCode();
        this.deliveryStatus = invoiceEntity.getDeliveryStatus();
        this.invoiceDate = invoiceEntity.getInvoiceDate();
        this.invoiceRegionCode = invoiceEntity.getInvoiceRegionCode();
        this.orderCode = invoiceEntity.getOrder().getOrderCode();
        this.orderProductVOList = new ArrayList<>();
        invoiceEntity.getOrder().getOrderProductList()
                .forEach(product-> this.orderProductVOList.add(new OrderProductVO(product.getProduct().getProductName(), product.getRequestProductCount()))
                );


    }
}
