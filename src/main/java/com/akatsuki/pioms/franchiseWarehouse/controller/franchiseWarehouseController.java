package com.akatsuki.pioms.franchiseWarehouse.controller;

import com.akatsuki.pioms.franchiseWarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.franchiseWarehouse.service.FranchiseWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/franchise/warehouse")
public class franchiseWarehouseController {

    @Autowired
    private FranchiseWarehouseService franchiseWarehouseService;

    @PutMapping("/toggleFavorite/{id}")
    public ResponseEntity<?> toggleFavorite(@PathVariable int id) {
        franchiseWarehouseService.toggleFavorite(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FranchiseWarehouse>> findAllFavorites() {
        List<FranchiseWarehouse> favorites = franchiseWarehouseService.findAllFavorites();
        return ResponseEntity.ok(favorites);
    }


}
