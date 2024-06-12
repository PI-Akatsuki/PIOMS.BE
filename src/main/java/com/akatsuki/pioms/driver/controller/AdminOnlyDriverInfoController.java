package com.akatsuki.pioms.driver.controller;

import com.akatsuki.pioms.driver.dto.DeliveryDriverDTO;
import com.akatsuki.pioms.driver.service.DeliveryDriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "[관리자]배송기사 관리 API", description = "Delivery Driver Management")
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
    public ResponseEntity<List<DeliveryDriverDTO>> getDriverList() {
        List<DeliveryDriverDTO> driverList = deliveryDriverService.findDriverList();
        return ResponseEntity.ok(driverList);
    }

    @Operation(summary = "배송기사 상세 조회", description = "배송기사 상세 정보를 조회합니다.")
    @GetMapping("/list/detail/{driverId}")
    public ResponseEntity<DeliveryDriverDTO> getDriverById(@PathVariable int driverId) {
        Optional<DeliveryDriverDTO> driver = deliveryDriverService.findDriverById(driverId);
        return driver.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "배송기사 등록", description = "신규 배송기사를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<String> registerDriver(
            @RequestBody DeliveryDriverDTO driverDTO
    ) {
        return deliveryDriverService.saveDriver(driverDTO);
    }

    @Operation(summary = "배송기사 정보 수정", description = "기존 배송기사의 정보를 수정합니다.")
    @PutMapping("/update/{driverId}")
    public ResponseEntity<String> updateDriver(
            @PathVariable int driverId,
            @RequestBody DeliveryDriverDTO updatedDriverDTO
    ) {
        return deliveryDriverService.updateDriver(driverId, updatedDriverDTO);
    }

    @Operation(summary = "배송기사 삭제", description = "기존 배송기사를 비활성화합니다.")
    @DeleteMapping("/delete/{driverId}")
    public ResponseEntity<String> deleteDriver(@PathVariable int driverId) {
        return deliveryDriverService.deleteDriver(driverId);
    }

    @Operation(summary = "배송기사 비밀번호 초기화", description = "기존 배송기사의 비밀번호를 초기화합니다.")
    @PostMapping("/reset-password/{driverId}")
    public ResponseEntity<String> resetDriverPassword(@PathVariable int driverId) {
        return deliveryDriverService.resetDriverPassword(driverId);
    }
}
