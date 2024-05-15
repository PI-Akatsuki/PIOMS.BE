package com.akatsuki.pioms.invoice.aggregate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseInvoiceOrderProduct {
    private String productName;
    private int requestProductCount;

    public ResponseInvoiceOrderProduct(String productName, int requestProductCount) {
        this.productName = productName;
        this.requestProductCount = requestProductCount;
    }
}
