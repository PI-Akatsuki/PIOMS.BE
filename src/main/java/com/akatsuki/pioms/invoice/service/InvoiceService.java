package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.aggregate.InvoiceEntity;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoiceList;
import com.akatsuki.pioms.order.aggregate.Order;

import java.util.List;

public interface InvoiceService {
    List<InvoiceEntity> getAllInvoiceList();
    InvoiceEntity postInvoice(Order order);

    InvoiceEntity putInvoice(int invoiceCode, DELIVERY_STATUS invoiceStatus);

    InvoiceEntity getInvoice(int invoiceCode);

    Boolean checkInvoiceStatus(int orderCode);

    InvoiceEntity saveInvoice(InvoiceEntity invoiceEntity);

    void deleteInvoice(InvoiceEntity invoiceEntity);
    InvoiceEntity getInvoiceByOrderCode(int orderCode);
}
