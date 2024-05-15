package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InvoiceServiceTest {

    InvoiceService invoiceService;
    InvoiceRepository invoiceRepository;
    static int adminCode= 1;
    static int franchiseCode =1;

    @Autowired
    public InvoiceServiceTest(InvoiceService invoiceService, InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
    }

    @BeforeEach
    void init(){

    }

    @Test
    void getAllInvoiceList() {
//        List<Invo>
    }

    @Test
    void postInvoice() {
    }

    @Test
    void putInvoice() {
    }

    @Test
    void getInvoice() {
    }

    @Test
    void checkInvoiceStatus() {
    }

    @Test
    void saveInvoice() {
    }

    @Test
    void deleteInvoice() {
    }

    @Test
    void getInvoiceByOrderCode() {
    }

    @Test
    void afterAcceptOrder() {
    }
}