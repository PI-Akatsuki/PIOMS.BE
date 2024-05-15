package com.akatsuki.pioms.invoice.repository;

import com.akatsuki.pioms.invoice.aggregate.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    Invoice findByOrderOrderCode(int orderCode);
}
