package com.akatsuki.pioms.invoice.controller;


import com.akatsuki.pioms.invoice.entity.InvoiceEntity;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.invoice.vo.ResponseInvoiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseInvoiceList> getInvoiceList(){
        ResponseInvoiceList responseInvoiceList = invoiceService.getAllInvoiceList();
        return ResponseEntity.ok(responseInvoiceList);
    }



}
