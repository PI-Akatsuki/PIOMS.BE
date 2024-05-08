package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.vo.ResponseInvoice;
import com.akatsuki.pioms.invoice.vo.ResponseInvoiceList;

public interface InvoiceService {
    ResponseInvoiceList getAllInvoiceList();

    ResponseInvoice putInvoice(int invoiceCode, String invoiceStatus);

    ResponseInvoice getInvoice(int invoiceCode);
}
