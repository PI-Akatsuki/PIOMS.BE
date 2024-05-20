package com.akatsuki.pioms.invoice.repository;

import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    Invoice findByOrderOrderCode(int orderCode);

    List<Invoice> findByOrderFranchiseFranchiseCode(int franchiseCode);

    boolean existsByOrderOrderCode(int orderCode);

    List<Invoice> findAllByOrderFranchiseAdminAdminCode(int adminCode);

    List<Invoice> findAllByOrderFranchiseFranchiseOwnerFranchiseOwnerCode(int franchiseOwnerCode);
}
