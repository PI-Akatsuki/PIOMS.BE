package com.akatsuki.pioms.driver.controller;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.service.DeliveryDriverService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("driver")
public class DriverOnlyDriverInfoController {

    private final DeliveryDriverService deliveryDriverService;

    @Autowired
    public DriverOnlyDriverInfoController(DeliveryDriverService deliveryDriverService) {
        this.deliveryDriverService = deliveryDriverService;
    }

    @Operation(summary = "배송기사 상세 조회", description = "배송기사 상세 정보를 조회합니다.")
    @GetMapping("/info/detail/{driverId}")
    public ResponseEntity<DeliveryDriver> getDriverById(@PathVariable int driverId) {
        Optional<DeliveryDriver> driver = deliveryDriverService.findDriverById(driverId);
        return driver.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
