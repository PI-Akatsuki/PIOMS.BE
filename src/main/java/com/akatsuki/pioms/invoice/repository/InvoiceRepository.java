package com.akatsuki.pioms.invoice.repository;

import com.akatsuki.pioms.invoice.aggregate.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity,Integer> {

    InvoiceEntity findByOrderOrderCode(int orderCode);
}
