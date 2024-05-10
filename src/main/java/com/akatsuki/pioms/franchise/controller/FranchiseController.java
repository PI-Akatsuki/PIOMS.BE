package com.akatsuki.pioms.franchise.controller;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(name = "프랜차이즈(가맹점)", description = "Franchise")
@RestController
@RequestMapping("franchise")
public class FranchiseController {

    private final FranchiseService franchiseService;

    @Autowired
    public FranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    // 프랜차이즈 전체 조회
    @Operation(summary = "프랜차이즈 전체 조회", description = "프랜차이즈 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<List<Franchise>> getAdminList() {
        List<Franchise> franchiseList = franchiseService.findFranchiseList();
        return ResponseEntity.ok(franchiseList);
    }

    @Operation(summary = "프랜차이즈 상세 조회", description = "프랜차이즈 상세 조회")
    @GetMapping("/list/detail/{franchiseCode}")
    public ResponseEntity<Franchise> getFranchiseById(@PathVariable int franchiseCode) {
        Optional<Franchise> franchise = franchiseService.findFranchiseById(franchiseCode);
        return franchise.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
