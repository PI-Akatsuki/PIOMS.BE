package com.akatsuki.pioms.frowner.controller;

import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.service.FranchiseOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("franchise/mypage")
@Tag(name = "[점주]점주 API")
public class FrOwnerOnlyFrOwnerController {
    public final FranchiseOwnerService franchiseOwnerService;

    @Autowired
    public FrOwnerOnlyFrOwnerController(FranchiseOwnerService franchiseOwnerService) {
        this.franchiseOwnerService = franchiseOwnerService;
    }

    // 상세 조회
    @Operation(summary = "프랜차이즈 점주 상세 조회", description = "프랜차이즈 점주 상세 조회")
    @GetMapping("/info/{franchiseOwnerCode}")
    public ResponseEntity<FranchiseOwnerDTO> getFranchiseOwnerById(@PathVariable int franchiseOwnerCode) {
        return franchiseOwnerService.findFranchiseOwnerById(franchiseOwnerCode);
    }

    // 오너 정보 수정
    @Operation(summary = "프랜차이즈 점주 정보 수정", description = "기존 프랜차이즈 점주의 정보를 수정합니다.")
    @PutMapping("/update/{franchiseOwnerCode}")
    public ResponseEntity<String> updateFranchiseOwner(
            @PathVariable int franchiseOwnerCode,
            @RequestBody FranchiseOwnerDTO updatedFranchiseOwner
    ) {
        return franchiseOwnerService.updateFranchiseOwner(franchiseOwnerCode, updatedFranchiseOwner);
    }

    @Operation(summary = "점주 이름을 통한 정보 조회 ")
    @GetMapping("/owner/{franchiseOwnerCode}")
    public ResponseEntity<FranchiseOwnerDTO> getFranchiseOwnerWithFranchiseName(@PathVariable int franchiseOwnerCode) {
        FranchiseOwnerDTO franchiseOwnerDTO = franchiseOwnerService.getFranchiseOwnerWithFranchiseName(franchiseOwnerCode);
        return ResponseEntity.ok(franchiseOwnerDTO);
    }
}
