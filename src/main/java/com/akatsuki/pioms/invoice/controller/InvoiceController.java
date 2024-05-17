package com.akatsuki.pioms.invoice.controller;


import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseInvoiceList> getInvoiceList(){
        List<InvoiceDTO> invoiceList = invoiceService.getAllInvoiceList();
        ResponseInvoiceList responseInvoiceList = new ResponseInvoiceList(invoiceList);
        return ResponseEntity.ok(responseInvoiceList);
    }

    @GetMapping("/{invoiceCode}")
    public ResponseEntity<ResponseInvoice> getInvoice(@PathVariable int invoiceCode){
        return ResponseEntity.ok(new ResponseInvoice(invoiceService.getInvoice(invoiceCode)));
    }

}
