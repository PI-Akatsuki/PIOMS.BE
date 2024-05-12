package com.akatsuki.pioms.driver.controller;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.service.DeliveryDriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "배송기사 관리", description = "Delivery Driver Management")
@RestController
@RequestMapping("admin/driver")
public class AdminOnlyDriverInfoController {

    private final DeliveryDriverService deliveryDriverService;

    @Autowired
    public AdminOnlyDriverInfoController(DeliveryDriverService deliveryDriverService) {
        this.deliveryDriverService = deliveryDriverService;
    }

    @Operation(summary = "배송기사 전체 목록 조회", description = "배송기사 전체 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<DeliveryDriver>> getDriverList() {
        List<DeliveryDriver> driverList = deliveryDriverService.findDriverList();
        return ResponseEntity.ok(driverList);
    }

    @Operation(summary = "배송기사 상세 조회", description = "배송기사 상세 정보를 조회합니다.")
    @GetMapping("/detail/{driverId}")
    public ResponseEntity<DeliveryDriver> getDriverById(@PathVariable int driverId) {
        Optional<DeliveryDriver> driver = deliveryDriverService.findDriverById(driverId);
        return driver.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "배송기사 등록", description = "배송기사를 등록합니다. (루트 관리자만 가능)")
    @PostMapping("/register")
    public ResponseEntity<String> registerDriver(
            @RequestBody DeliveryDriver driver,
            @RequestParam int requestorAdminCode
    ) {
        return deliveryDriverService.saveDriver(driver, requestorAdminCode);
    }
}
