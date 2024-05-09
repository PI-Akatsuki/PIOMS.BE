package com.akatsuki.pioms.invoice.repository;

import com.akatsuki.pioms.invoice.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity,Integer> {

}
