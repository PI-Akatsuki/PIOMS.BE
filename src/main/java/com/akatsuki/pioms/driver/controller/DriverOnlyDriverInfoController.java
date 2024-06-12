package com.akatsuki.pioms.driver.controller;

import com.akatsuki.pioms.driver.dto.DeliveryDriverDTO;
import com.akatsuki.pioms.driver.service.DeliveryDriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("driver")
@Tag(name = "[배송기사] 배송기사 API")
public class DriverOnlyDriverInfoController {

    private final DeliveryDriverService deliveryDriverService;

    @Autowired
    public DriverOnlyDriverInfoController(DeliveryDriverService deliveryDriverService) {
        this.deliveryDriverService = deliveryDriverService;
    }

    @Operation(summary = "배송기사 상세 조회", description = "배송기사 상세 정보를 조회합니다.")
    @GetMapping("/info/detail/{driverId}")
    public ResponseEntity<DeliveryDriverDTO> getDriverById(@PathVariable int driverId) {
        Optional<DeliveryDriverDTO> driver = deliveryDriverService.findDriverById(driverId);
        return driver.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "배송기사 정보 수정", description = "기존 배송기사의 정보를 수정합니다.")
    @PutMapping("/update/{driverId}")
    public ResponseEntity<String> updateDriver(
            @PathVariable int driverId,
            @RequestBody DeliveryDriverDTO updatedDriverDTO
    ) {
        return deliveryDriverService.updateDriver(driverId, updatedDriverDTO);
    }
}
