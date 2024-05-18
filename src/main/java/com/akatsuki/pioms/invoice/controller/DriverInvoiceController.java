package com.akatsuki.pioms.invoice.controller;

import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseDriverInvoice;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "배송상태조회", description = "배송기사별 배송상태조회 - 전체 및 상세조회")
@RestController
@RequestMapping("driver/{driverCode}")
public class DriverInvoiceController {

    private InvoiceService invoiceService;

    @Autowired
    public DriverInvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Operation(summary = "배송상태조회", description = "배송기사코드로 담당지역의 배송상태 전체조회")
    @GetMapping("/invoice/list")
    public ResponseEntity<List<ResponseDriverInvoice>> getAllDriverInvoiceList(@PathVariable int driverCode) {
        List<ResponseDriverInvoice> invoiceDTOList = invoiceService.getAllDriverInvoiceList(driverCode);
        System.out.println("invoiceDTOList = " + invoiceDTOList);
        if ( invoiceDTOList == null||invoiceDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(invoiceDTOList);
    }

    @Operation(summary = "배송상태조회", description = "배송기사코드로 담당지역의 배송상태(배송전) 상세조회")
    @GetMapping("/invoice/status/before_delivery")
    public ResponseEntity<List<ResponseDriverInvoice>> getStatusDeliveryDriverInvoiceList(@PathVariable int driverCode) {
        List<ResponseDriverInvoice> responseDriverInvoice = invoiceService.getStatusDeliveryDriverInvoiceList(driverCode);
        return ResponseEntity.ok().body(responseDriverInvoice);
    }


}
