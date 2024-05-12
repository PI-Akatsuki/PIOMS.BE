package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.aggregate.InvoiceEntity;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoiceList;
import com.akatsuki.pioms.order.aggregate.Order;

public interface InvoiceService {
    ResponseInvoiceList getAllInvoiceList();
    InvoiceEntity postInvoice(Order order);

    ResponseInvoice putInvoice(int invoiceCode, DELIVERY_STATUS invoiceStatus);

    ResponseInvoice getInvoice(int invoiceCode);

    Boolean checkInvoiceStatus(int orderCode);

    InvoiceEntity saveInvoice(InvoiceEntity invoiceEntity);
}
