package com.akatsuki.pioms.invoice.controller;


import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoiceList;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "[관리자]송장/배송 API", description = "관리자의 송장[배송] 관련 API 입니다.")
public class AdminInvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public AdminInvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoice/list")
    @Operation(summary = "모든 가맹점의 송장 조회",description = "관리자가 관리하는 모든 가맹점의 송장들을 조회합니다.")
    public ResponseEntity<ResponseInvoiceList> getInvoiceList(){
        List<InvoiceDTO> invoiceList = invoiceService.getAdminInvoiceList();
        if (invoiceList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        ResponseInvoiceList responseInvoiceList = new ResponseInvoiceList(invoiceList);
        return ResponseEntity.ok(responseInvoiceList);
    }

    @GetMapping("/invoice/{invoiceCode}")
    @Operation(summary = "가맹점의 송장 상세 조회")
    public ResponseEntity<ResponseInvoice> getInvoice(@RequestParam int adminCode ,@PathVariable int invoiceCode){
        InvoiceDTO invoiceDTO = invoiceService.getInvoiceByAdminCode(adminCode,invoiceCode);
        if (invoiceDTO==null)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(new ResponseInvoice(invoiceDTO));
    }

    @PutMapping("/invoice/{invoiceCode}/{invoiceStatus}")
    @Operation(summary = "송장의 배송 상태 변경")
    public ResponseEntity<ResponseInvoice> putInvoice(@RequestParam int adminCode, @PathVariable int invoiceCode, @PathVariable DELIVERY_STATUS invoiceStatus){
        InvoiceDTO invoice = invoiceService.putInvoice(adminCode,invoiceCode, invoiceStatus);

        if (invoice==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(new ResponseInvoice(invoice));
    }

}
