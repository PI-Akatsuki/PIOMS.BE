package com.akatsuki.pioms.franchise.controller;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "프랜차이즈(가맹점)", description = "Franchise")
@RestController
@RequestMapping("admin/franchise")
public class AdminOnlyFranchiseController {

    private final FranchiseService franchiseService;

    @Autowired
    public AdminOnlyFranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    // 프랜차이즈 전체 조회
    @Operation(summary = "프랜차이즈 전체 조회", description = "프랜차이즈 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<List<Franchise>> getAdminList() {
        List<Franchise> franchiseList = franchiseService.findFranchiseList();
        return ResponseEntity.ok(franchiseList);
    }

    // 프랜차이즈 상세 조회
    @Operation(summary = "프랜차이즈 상세 조회", description = "프랜차이즈 상세 조회")
    @GetMapping("/list/detail/{franchiseCode}")
    public ResponseEntity<Franchise> getFranchiseById(@PathVariable int franchiseCode) {
        Optional<Franchise> franchise = franchiseService.findFranchiseById(franchiseCode);
        return franchise.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 프랜차이즈 등록
    @PostMapping("/register")
    public ResponseEntity<String> registerFranchise(
            @RequestBody Franchise franchise,
            @RequestParam int requestorAdminCode
    ) {
        return franchiseService.saveFranchise(franchise, requestorAdminCode);
    }

    // 프랜차이즈 수정
    @PutMapping("/update")
    public ResponseEntity<String> updateFranchise(
            @RequestParam int requestorCode,
            @RequestBody Franchise updatedFranchise) {
        return franchiseService.updateFranchise(updatedFranchise.getFranchiseCode(), updatedFranchise, requestorCode, false);

    }

    // 프랜차이즈 삭제

}
