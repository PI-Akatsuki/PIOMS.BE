package com.akatsuki.pioms.driver.controller;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.aggregate.DeliveryRegion;
import com.akatsuki.pioms.driver.service.DeliveryDriverService;
import com.akatsuki.pioms.driver.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("driver")
public class DriverOnlyDriverInfoController {

    private final DeliveryDriverService deliveryDriverService;
    private final   DeliveryService deliveryService;

    @Autowired
    public DriverOnlyDriverInfoController(DeliveryDriverService deliveryDriverService, DeliveryService deliveryService) {
        this.deliveryDriverService = deliveryDriverService;
        this.deliveryService = deliveryService;
    }


    @Operation(summary = "배송기사 상세 조회", description = "배송기사 상세 정보를 조회합니다.")
    @GetMapping("/info/detail/{driverId}")
    public ResponseEntity<DeliveryDriver> getDriverById(@PathVariable int driverId) {
        Optional<DeliveryDriver> driver = deliveryDriverService.findDriverById(driverId);
        return driver.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "배송기사 정보 수정", description = "기존 배송기사의 정보를 수정합니다.")
    @PutMapping("/update/{driverId}")
    public ResponseEntity<String> updateDriver(
            @PathVariable int driverId,
            @RequestBody DeliveryDriver updatedDriver,
            @RequestParam(required = false) Integer requestorAdminCode,
            @RequestParam(required = false) Integer requestorDriverCode
    ) {
        return deliveryDriverService.updateDriver(driverId, updatedDriver, requestorAdminCode, requestorDriverCode);
    }
}
