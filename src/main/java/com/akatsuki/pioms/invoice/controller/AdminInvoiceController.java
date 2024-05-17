package com.akatsuki.pioms.invoice.controller;


import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoiceList;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminInvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public AdminInvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("{adminCode}/invoice/list")
    public ResponseEntity<ResponseInvoiceList> getInvoiceList(@PathVariable int adminCode){
        List<InvoiceDTO> invoiceList = invoiceService.getAllInvoiceList();
        ResponseInvoiceList responseInvoiceList = new ResponseInvoiceList(invoiceList);
        return ResponseEntity.ok(responseInvoiceList);
    }

    @GetMapping("/{adminCode}/invoice/{invoiceCode}")
    public ResponseEntity<ResponseInvoice> getInvoice(@PathVariable int invoiceCode){
        return ResponseEntity.ok(new ResponseInvoice(invoiceService.getInvoice(invoiceCode)));
    }

    @PutMapping("/{adminCode}/invoice/{invoiceCode}/{invoiceStatus}")
    public ResponseEntity<ResponseInvoice> putInvoice(@PathVariable int invoiceCode, @PathVariable DELIVERY_STATUS invoiceStatus){
        InvoiceDTO invoice = invoiceService.putInvoice(invoiceCode, invoiceStatus);
        return ResponseEntity.ok(new ResponseInvoice(invoice));
    }





}
