package com.akatsuki.pioms.frwarehouse.controller;

import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
