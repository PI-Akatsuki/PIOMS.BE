package com.akatsuki.pioms.invoice.controller;

import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.ResponseDriverInvoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseInvoiceDetail;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "[배송기사]배송상태조회 API", description = "배송기사별 배송상태조회관련 전체 및 상세조회 API")
@RestController
@RequestMapping("driver")
public class DriverInvoiceController {

    private InvoiceService invoiceService;

    @Autowired
    public DriverInvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Operation(summary = "배송상태조회", description = "배송기사코드로 담당지역의 배송상태 전체조회")
    @GetMapping("/invoice/status/{driverCode}")
    public ResponseEntity<List<ResponseDriverInvoice>> getAllDriverInvoiceList(@PathVariable int driverCode) {
        List<ResponseDriverInvoice> invoiceDTOList = invoiceService.getAllDriverInvoiceList(driverCode);
        if (invoiceDTOList == null || invoiceDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(invoiceDTOList);
    }

    @Operation(summary = "배송상태조회-배송전", description = "배송기사코드로 담당지역의 배송상태(배송전) 상세조회")
    @GetMapping("/invoice/status/before_delivery/{driverCode}")
    public ResponseEntity<List<ResponseDriverInvoice>> getStatusBeforeDeliveryDriverInvoiceList(@PathVariable int driverCode) {
        List<ResponseDriverInvoice> responseDriverInvoice = invoiceService.getStatusBeforeDeliveryDriverInvoiceList(driverCode);
        return ResponseEntity.ok().body(responseDriverInvoice);
    }

    @Operation(summary = "배송상태조회-배송중", description = "베송기사코드로 담당지역의 배송상태(배송중) 상세조회")
    @GetMapping("/invoice/status/ing_delivery/{driverCode}")
    public ResponseEntity<List<ResponseDriverInvoice>> getStatusIngDeliveryDriverInvoiceList(@PathVariable int driverCode) {
        List<ResponseDriverInvoice> responseDriverInvoice = invoiceService.getStatusIngDeliveryDriverInvoiceList(driverCode);
        return ResponseEntity.ok().body(responseDriverInvoice);
    }

    @Operation(summary = "배송상태조회-배송완료", description = "배송기사코드로 담당지역의 배송상태(배송완료) 상세조회")
    @GetMapping("/invoice/status/complete_delivery/{driverCode}")
    public ResponseEntity<List<ResponseDriverInvoice>> getStatusCompleteDeliveryDriverInvoiceList(@PathVariable int driverCode) {
        List<ResponseDriverInvoice> responseDriverInvoice = invoiceService.getStatusCompleteDeliveryDriverInvoiceList(driverCode);
        return ResponseEntity.ok().body(responseDriverInvoice);
    }

    @Operation(summary = "배송상태변경", description = "배송기사가 배송상태 수정")
    @PutMapping("/invoice/status/{invoiceCode}/delivery/{deliveryStatus}")
    public ResponseEntity<Boolean> modifyInvoiceStatusByDriver(@PathVariable int invoiceCode, @PathVariable DELIVERY_STATUS deliveryStatus) {
        boolean result = invoiceService.modifyInvoiceStatusByDriver(invoiceCode, 0, deliveryStatus);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "배송전 상태 송장 수 조회", description = "배송기사가 가진 배송전 송장 수 조회")
    @GetMapping("/{driverCode}/before-delivery/count")
    public int countStatusBeforeDeliveryDriverInvoices(@PathVariable int driverCode) {
        return invoiceService.countStatusBeforeDeliveryDriverInvoices(driverCode);
    }

    @Operation(summary = "배송전 상태 송장 수 조회", description = "배송기사가 가진 배송중 송장 수 조회")
    @GetMapping("/{driverCode}/ing-delivery/count")
    public int countStatusIngDeliveryDriverInvoices(@PathVariable int driverCode) {
        return invoiceService.countStatusIngDeliveryDriverInvoices(driverCode);
    }

    @Operation(summary = "배송전 상태 송장 수 조회", description = "배송기사가 가진 배송완료 송장 수 조회")
    @GetMapping("/{driverCode}/complete-delivery/count")
    public int countStatusCompleteDeliveryDriverInvoices(@PathVariable int driverCode) {
        return invoiceService.countStatusCompleteDeliveryDriverInvoices(driverCode);
    }

    @Operation(summary = "배송 송장 상세조회", description = "배송기사가 가진 송장 상세조회")
    @GetMapping("/{driverCode}/{invoiceCode}/details")
    public ResponseInvoiceDetail getIovoiceDetails (@PathVariable int driverCode, @PathVariable int invoiceCode) {
        return invoiceService.getInvoiceDetail(invoiceCode);
    }
}
