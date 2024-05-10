package com.akatsuki.pioms.invoice.aggregate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Getter
@NoArgsConstructor
@ToString
public class ResponseInvoiceList {
    List<ResponseInvoice> invoiceList;

    public ResponseInvoiceList(List<ResponseInvoice> responseInvoice) {
        this.invoiceList = responseInvoice;
    }
}
