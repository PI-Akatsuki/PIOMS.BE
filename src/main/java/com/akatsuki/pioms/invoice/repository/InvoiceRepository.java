package com.akatsuki.pioms.invoice.repository;

import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    Invoice findByOrderOrderCode(int orderCode);

    List<Invoice> findByOrderFranchiseFranchiseCode(int franchiseCode);

    boolean existsByOrderOrderCode(int orderCode);

    List<Invoice> findAllByOrderFranchiseAdminAdminCode(int adminCode);

    List<Invoice> findAllByOrderFranchiseFranchiseOwnerFranchiseOwnerCode(int franchiseOwnerCode);

    List<Invoice> findAllByOrderFranchiseDeliveryDriverDriverCode(int driverCode);

    @Query("SELECT i FROM Invoice i ORDER BY i.invoiceDate DESC")
    List<Invoice> findAllByOrderDesc();

    List<Invoice> findAllByOrderFranchiseAdminAdminCodeOrderByInvoiceDateDesc(int adminCode);
}
