package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseDriverInvoice;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.order.dto.OrderDTO;

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

    // 배송상태조회 - 전체조회
    List<ResponseDriverInvoice> getAllDriverInvoiceList(int driverCode);

    // 배송상태조회 - 배송기사코드와 담당지역의 배송상태에 따른 상세조회
    List<ResponseDriverInvoice> getStatusDeliveryDriverInvoiceList(int driverCode);
}
