package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.aggregate.InvoiceEntity;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoiceList;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;

public interface InvoiceService {
    ResponseInvoiceList getAllInvoiceList();
    InvoiceDTO postInvoice(OrderDTO order);

    ResponseInvoice putInvoice(int invoiceCode, DELIVERY_STATUS invoiceStatus);

    ResponseInvoice getInvoice(int invoiceCode);

    Boolean checkInvoiceStatus(int orderCode);

    InvoiceDTO saveInvoice(InvoiceDTO invoiceEntity);

    void deleteInvoice(InvoiceDTO invoiceEntity);
    InvoiceDTO getInvoiceByOrderCode(int orderCode);
}
