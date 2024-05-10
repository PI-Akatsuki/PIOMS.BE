package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.vo.ResponseInvoice;
import com.akatsuki.pioms.invoice.vo.ResponseInvoiceList;

public interface InvoiceService {
    ResponseInvoiceList getAllInvoiceList();

    ResponseInvoice putInvoice(int invoiceCode, DELIVERY_STATUS invoiceStatus);

    ResponseInvoice getInvoice(int invoiceCode);

    Boolean checkInvoiceStatus(int orderCode);
}
