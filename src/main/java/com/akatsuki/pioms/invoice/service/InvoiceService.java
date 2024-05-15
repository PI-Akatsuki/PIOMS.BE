package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> getAllInvoiceList();
    InvoiceDTO postInvoice(int orderCode, int franchiseCode, DELIVERY_DATE franchiseDeliveryDate, LocalDateTime orderDateTime);

    InvoiceDTO putInvoice(int invoiceCode, DELIVERY_STATUS invoiceStatus);

    InvoiceDTO getInvoice(int invoiceCode);

    Boolean checkInvoiceStatus(int orderCode);

    InvoiceDTO saveInvoice(Invoice invoiceEntity);

    void deleteInvoice(Invoice invoiceEntity);

    InvoiceDTO getInvoiceByOrderCode(int orderCode);

    void afterAcceptOrder(int orderCode, int franchiseCode, DELIVERY_DATE deliveryStatus, LocalDateTime orderDate);

}
