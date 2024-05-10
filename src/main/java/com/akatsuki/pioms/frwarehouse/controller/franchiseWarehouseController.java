package com.akatsuki.pioms.frwarehouse.controller;

import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.aggregate.RequestFranchiseWarehouseUpdate;
import com.akatsuki.pioms.frwarehouse.aggregate.ResponseFranchiseWarehouseUpdate;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/warehouse")
public class franchiseWarehouseController {

    private FranchiseWarehouseService franchiseWarehouseService;

    @Autowired
    public franchiseWarehouseController(FranchiseWarehouseService franchiseWarehouseService) {
        this.franchiseWarehouseService = franchiseWarehouseService;
    }

    @GetMapping("")
    public ResponseEntity<List<FranchiseWarehouse>> getAllWarehous() {
        return ResponseEntity.ok().body(franchiseWarehouseService.getAllWarehouse());
    }

    @GetMapping("/{franchiseWarehouseCode}")
    @Operation(summary = "franchiseWarehouseCode로 franchiseWarehouse 조회")
    public ResponseEntity<Optional<FranchiseWarehouse>> getWarehouseByWarehouseCode(@PathVariable int franchiseWarehouseCode) {
        Optional<FranchiseWarehouse> franchiseWarehouse = franchiseWarehouseService.getWarehouseByWarehouseCode(franchiseWarehouseCode);
        return ResponseEntity.ok().body(franchiseWarehouse);
    }

    @PostMapping("/update/{franchiseWarehouseCode}")
    @Operation(summary = "사라졌을 상품을 위한 재고 수정 기능")
    public ResponseEntity<ResponseFranchiseWarehouseUpdate> updateWarehouseCount(@PathVariable int franchiseWarehouseCode, @RequestBody RequestFranchiseWarehouseUpdate request) {
        ResponseFranchiseWarehouseUpdate response = franchiseWarehouseService.updateWarehouseCount(franchiseWarehouseCode,request);
        return ResponseEntity.ok().body(response);
    }
}
