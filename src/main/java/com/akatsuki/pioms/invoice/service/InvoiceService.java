package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseDriverInvoice;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceService {

    /**
     * .@Copyright 32173594@dankook.ac.kr
     * <br><br><br>
     * <h1>Invoice Service</h1>
     *     <br>
     * <h1>Admin Functions</h1>
     * <h2>1. Get Admin Invoice List : 관리자가 관리하고 있는 모든 가맹점의 송장 목록 반환</h2>
     * <h2>2. Get Invoice By Admin Code : 관리자가 관리하고 있는 모든 가맹점의 송장 목록 중 하나 상세 조회 </h2>
     * <h2>3.  putInvoice : 관리자의 배송 상태 수정 </h2>
     * <br>
     *
     * <h1> Franchise Owner Functions</h1>
     * <h2>1.Get Franchise Invoice List : 점주의 모든 송장 목록 반환 </h2>
     * <h2>2.Get Invoice By Franchise Owner Code : 점주의 송장 상세 조회 </h2>
     * <br>
     *
     * <h1> INNER FUNCTIONS </h1>
     * <h2>1. After Accept Order : 관리자가 주문을 승인한 이후 새로운 송장을 생성하는 로직 </h2>
     * <h2>2. Check Invoice Status : 주문 배송 상태 조회하기 위한 로직 </h2>
     * <h2>3. Get Invoice By Order Code : 주문 코드를 통해 송장 조회하는 로직</h2>
     * <h2>4. Delete Invoice : 아직 처리 되지 않은 주문 삭제 </h2>
     * */

    List<InvoiceDTO> getAdminInvoiceList(int adminCode);
    InvoiceDTO getInvoiceByAdminCode(int adminCode, int invoiceCode);
    InvoiceDTO putInvoice(int adminCode,int invoiceCode, DELIVERY_STATUS invoiceStatus);

    List<InvoiceDTO> getFranchiseInvoiceList(int franchiseOwnerCode);
    InvoiceDTO getInvoiceByFranchiseOwnerCode(int franchiseOwnerCode, int invoiceCode);
    
    void afterAcceptOrder(OrderDTO orderEntity);

    Boolean checkInvoiceStatus(int orderCode);
    InvoiceDTO getInvoiceByOrderCode(int orderCode);
    boolean deleteInvoice(int franchiseOwnerCode, int invoiceCode);

    // 배송상태조회 - 전체조회
    List<ResponseDriverInvoice> getAllDriverInvoiceList(int driverCode);

}
