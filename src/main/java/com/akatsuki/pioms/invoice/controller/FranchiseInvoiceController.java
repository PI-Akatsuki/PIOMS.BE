package com.akatsuki.pioms.invoice.controller;


import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoiceList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/franchise")
public class FranchiseInvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public FranchiseInvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
    // 가맹점은 조회만 가능하다.
    @GetMapping("/invoice/list")
    public ResponseEntity<ResponseInvoiceList> getInvoiceList(@RequestParam int franchiseOwnerCode){
        List<InvoiceDTO> invoiceList = invoiceService.getFranchiseInvoiceList(franchiseOwnerCode);
        if (invoiceList.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseInvoiceList(invoiceList));
        return ResponseEntity.ok(new ResponseInvoiceList(invoiceList));
    }

    @GetMapping("/invoice/{invoiceCode}")
    public ResponseEntity<ResponseInvoice> getInvoice(@RequestParam int franchiseOwnerCode,@PathVariable int invoiceCode){
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceByFranchiseOwnerCode(franchiseOwnerCode,invoiceCode);
        if (invoiceDTO==null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(new ResponseInvoice(invoiceDTO));
    }


    // 🥸dummy🥸
//    @DeleteMapping("/invoice/{invoiceCode}")
//    public ResponseEntity<ResponseInvoice> deleteInvoice(@PathVariable int franchiseOwnerCode,@PathVariable int invoiceCode){
//
//        boolean flag = invoiceService.deleteInvoice(franchiseOwnerCode,invoiceCode);
//
//        if (!flag)
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
//        return ResponseEntity.ok().build();
//    }

}
