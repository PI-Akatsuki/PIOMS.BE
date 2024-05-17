package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> getAllInvoiceList();
    InvoiceDTO postInvoice(OrderDTO order);

    InvoiceDTO putInvoice(int adminCode,int invoiceCode, DELIVERY_STATUS invoiceStatus);

    Boolean checkInvoiceStatus(int orderCode);

    InvoiceDTO saveInvoice(Invoice invoiceEntity);

    boolean deleteInvoice(int franchiseOwnerCode, int invoiceCode);

    InvoiceDTO getInvoiceByOrderCode(int orderCode);

    void afterAcceptOrder(OrderDTO orderEntity);

    List<InvoiceDTO> getAdminInvoiceList(int adminCode);

    InvoiceDTO getInvoiceByAdminCode(int adminCode, int invoiceCode);

    List<InvoiceDTO> getFranchiseInvoiceList(int franchiseOwnerCode);

    InvoiceDTO getInvoiceByFranchiseOwnerCode(int franchiseOwnerCode, int invoiceCode);
}
