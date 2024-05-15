package com.akatsuki.pioms.invoice.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@ToString
public class ResponseInvoiceList {
    List<ResponseInvoice> invoiceList;

    public ResponseInvoiceList() {
        this.invoiceList = new ArrayList<>();
    }

    public ResponseInvoiceList(List<InvoiceEntity> invoiceList) {
        this.invoiceList = new ArrayList<>();
        invoiceList.forEach( invoiceDTO -> {
            this.invoiceList.add(new ResponseInvoice(invoiceDTO));
        });
    }
}
