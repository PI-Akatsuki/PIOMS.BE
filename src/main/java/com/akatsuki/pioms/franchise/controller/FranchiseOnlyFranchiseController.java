package com.akatsuki.pioms.franchise.controller;

import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/franchise")
@Tag(name = "[점주]가맹점 API")
public class FranchiseOnlyFranchiseController {

    private final FranchiseService franchiseService;

    @Autowired
    public FranchiseOnlyFranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    @Operation(summary = "프랜차이즈 상세 조회", description = "프랜차이즈 상세 정보를 조회합니다.")
    @GetMapping("/detail/{franchiseCode}")
    public ResponseEntity<FranchiseDTO> getFranchiseById(@PathVariable int franchiseCode) {
        Optional<FranchiseDTO> franchiseOptional = franchiseService.findFranchiseById(franchiseCode);
        return franchiseOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "프랜차이즈 정보 수정", description = "기존 프랜차이즈의 정보를 수정합니다.")
    @PutMapping("/update/{franchiseCode}")
    public ResponseEntity<String> updateFranchise(
            @PathVariable int franchiseCode,
            @RequestBody FranchiseDTO updatedFranchiseDTO
    ) {
        return franchiseService.updateFranchise(franchiseCode, updatedFranchiseDTO);
    }
}
