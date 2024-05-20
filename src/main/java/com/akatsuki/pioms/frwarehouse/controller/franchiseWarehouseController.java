package com.akatsuki.pioms.frwarehouse.controller;

import com.akatsuki.pioms.frwarehouse.aggregate.RequestFranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.aggregate.ResponseFranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/warehouse")
public class franchiseWarehouseController {

    private final FranchiseWarehouseService franchiseWarehouseService;

    @Autowired
    public franchiseWarehouseController(FranchiseWarehouseService franchiseWarehouseService) {
        this.franchiseWarehouseService = franchiseWarehouseService;
    }

    @GetMapping("")
    public ResponseEntity<List<FranchiseWarehouseDTO>> getAllWarehous() {
        return ResponseEntity.ok().body(franchiseWarehouseService.getAllWarehouse());
    }

    @GetMapping("/list/detail/{franchiseWarehouseCode}")
    @Operation(summary = "franchiseWarehouseCode로 franchiseWarehouse 조회")
    public ResponseEntity<List<ResponseFranchiseWarehouse>> getWarehouseByWarehouseCode(@PathVariable int franchiseWarehouseCode) {
        List<FranchiseWarehouseDTO> franchiseWarehouseDTOS = franchiseWarehouseService.getWarehouseByWarehouseCode(franchiseWarehouseCode);
        List<ResponseFranchiseWarehouse> responseWarehouse = new ArrayList<>();
        franchiseWarehouseDTOS.forEach(franchiseWarehouseDTO -> {
            responseWarehouse.add(new ResponseFranchiseWarehouse(franchiseWarehouseDTO));
        });
        return ResponseEntity.ok(responseWarehouse);
    }

    @PostMapping("/update/{franchiseWarehouseCode}")
    @Operation(summary = "사라졌을 상품을 위한 재고 수정 기능")
    public ResponseEntity<String> updateWarehouseCount(@PathVariable int franchiseWarehouseCode, @RequestBody RequestFranchiseWarehouse request, int requesterAdminCode) {
        return franchiseWarehouseService.updateWarehouseCount(franchiseWarehouseCode,request, requesterAdminCode);
    }


    @GetMapping("/{franchiseOwnerCode}/list")
    public ResponseEntity<List<FranchiseWarehouseDTO>> getFrWarehouseList(@PathVariable int franchiseOwnerCode){
        return ResponseEntity.ok(franchiseWarehouseService.getFrWarehouseList(franchiseOwnerCode));
    }
}
