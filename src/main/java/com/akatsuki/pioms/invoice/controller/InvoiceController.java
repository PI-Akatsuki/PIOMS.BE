package com.akatsuki.pioms.invoice.controller;


import com.akatsuki.pioms.invoice.entity.InvoiceEntity;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.invoice.vo.ResponseInvoice;
import com.akatsuki.pioms.invoice.vo.ResponseInvoiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{invoiceCode}")
    public ResponseEntity<ResponseInvoice> getInvoice(@PathVariable int invoiceCode){
        return ResponseEntity.ok(invoiceService.getInvoice(invoiceCode));
    }

    @PutMapping("/{invoiceCode}/{invoiceStatus}")
    public ResponseEntity<ResponseInvoice> putInvoice(@PathVariable int invoiceCode, @PathVariable String invoiceStatus){
        ResponseInvoice invoice = invoiceService.putInvoice(invoiceCode, invoiceStatus);
        return ResponseEntity.ok(invoice);
    }



}