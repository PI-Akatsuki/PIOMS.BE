package com.akatsuki.pioms.invoice.aggregate;

import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
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
    public ResponseInvoiceList(List<InvoiceDTO> invoiceList) {
        this.invoiceList = new ArrayList<>();
        invoiceList.forEach( invoiceDTO -> {
            this.invoiceList.add(new ResponseInvoice(invoiceDTO));
        });
    }
}
