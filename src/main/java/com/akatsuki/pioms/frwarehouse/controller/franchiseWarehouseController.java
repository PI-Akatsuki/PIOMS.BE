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
    public ResponseEntity<FranchiseWarehouse> getWarehouseByWarehouseCode(@PathVariable int franchiseWarehouseCode) {
        return ResponseEntity.ok().body(franchiseWarehouseService.getWarehouseByWarehouseCode(franchiseWarehouseCode));
    }

    @PostMapping("/update/{franchiseWarehouseCode}")
    @Operation(summary = "사라졌을 상품을 위한 재고 수정 기능")
    public ResponseEntity<String> updateWarehouseCount(@PathVariable int franchiseWarehouseCode, @RequestBody RequestFranchiseWarehouseUpdate request, int requesterAdminCode) {
        return franchiseWarehouseService.updateWarehouseCount(franchiseWarehouseCode,request, requesterAdminCode);
    }

    @PutMapping("/toggleFavorite/{franchiseWarehouseCode}")
    @Operation(summary = "즐겨찾기 추가,삭제 기능")
    public ResponseEntity<?> toggleFavorite(@PathVariable int franchiseWarehouseCode){
        franchiseWarehouseService.toggleFavorite(franchiseWarehouseCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    @Operation(summary = "즐겨찾기 상품 조회")
    public ResponseEntity<List<FranchiseWarehouse>> findAllFavorites() {
        List<FranchiseWarehouse> favorites = franchiseWarehouseService.findAllFavorites();
        return ResponseEntity.ok(favorites);
    }
}
