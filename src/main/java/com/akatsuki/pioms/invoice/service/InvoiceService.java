package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.aggregate.InvoiceEntity;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoiceList;
import com.akatsuki.pioms.order.aggregate.Order;

import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> getAllInvoiceList();
    InvoiceDTO postInvoice(Order order);

    InvoiceDTO putInvoice(int invoiceCode, DELIVERY_STATUS invoiceStatus);

    InvoiceDTO getInvoice(int invoiceCode);

    Boolean checkInvoiceStatus(int orderCode);

    InvoiceDTO saveInvoice(InvoiceDTO invoiceEntity);

    void deleteInvoice(InvoiceDTO invoiceEntity);
    InvoiceDTO getInvoiceByOrderCode(int orderCode);
}
