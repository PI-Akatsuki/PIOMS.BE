package com.akatsuki.pioms.franchise.controller;

import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/franchise")
public class AdminOnlyFranchiseController {

    private final FranchiseService franchiseService;

    @Autowired
    public AdminOnlyFranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @Operation(summary = "프랜차이즈 전체 조회", description = "전체 프랜차이즈를 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<FranchiseDTO>> getFranchiseList() {
        List<FranchiseDTO> franchiseList = franchiseService.findFranchiseList();
        return ResponseEntity.ok(franchiseList);
    }

    @Operation(summary = "프랜차이즈 상세 조회", description = "프랜차이즈 상세 정보를 조회합니다.")
    @GetMapping("/detail/{franchiseCode}")
    public ResponseEntity<FranchiseDTO> getFranchiseById(@PathVariable int franchiseCode) {
        Optional<FranchiseDTO> franchise = franchiseService.findFranchiseById(franchiseCode);
        return franchise.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "프랜차이즈 등록", description = "신규 프랜차이즈를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<String> registerFranchise(
            @RequestBody FranchiseDTO franchiseDTO
    ) {
        return franchiseService.saveFranchise(franchiseDTO);
    }

    @Operation(summary = "프랜차이즈 정보 수정", description = "기존 프랜차이즈의 정보를 수정합니다.")
    @PutMapping("/update/{franchiseCode}")
    public ResponseEntity<String> updateFranchise(
            @PathVariable int franchiseCode,
            @RequestBody FranchiseDTO updatedFranchiseDTO,
            @RequestParam int requestorCode
    ) {
        return franchiseService.updateFranchise(franchiseCode, updatedFranchiseDTO, requestorCode, false);
    }

    @Operation(summary = "프랜차이즈 삭제", description = "기존 프랜차이즈를 삭제합니다.")
    @DeleteMapping("/delete/{franchiseCode}")
    public ResponseEntity<String> deleteFranchise(@PathVariable int franchiseCode) {
        return franchiseService.deleteFranchise(franchiseCode);
    }
}
