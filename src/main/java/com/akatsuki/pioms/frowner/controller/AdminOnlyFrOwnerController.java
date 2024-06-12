package com.akatsuki.pioms.frowner.controller;

import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.service.FranchiseOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/franchise/owner")
@Tag(name = "[관리자]점주 API")
public class AdminOnlyFrOwnerController {

    private final FranchiseOwnerService franchiseOwnerService;

    @Autowired
    public AdminOnlyFrOwnerController(FranchiseOwnerService franchiseOwnerService) {
        this.franchiseOwnerService = franchiseOwnerService;
    }

    // 전체 조회
    @Operation(summary = "점주 전체 조회", description = "점주 전체 목록을 조회하며, 담당 점포와 관리자도 함께 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<FranchiseOwnerDTO>> getOwnerListWithFranchiseAndAdmin() {

        List<FranchiseOwnerDTO> ownerList = franchiseOwnerService.findAllFranchiseOwners();

        return ResponseEntity.ok(ownerList);
    }

    // 상세조회
    @Operation(summary = "프랜차이즈 점주 상세 조회", description = "프랜차이즈 점주 상세 조회")
    @GetMapping("/detail/{franchiseOwnerCode}")
    public ResponseEntity<FranchiseOwnerDTO> getFranchiseOwnerById(@PathVariable int franchiseOwnerCode) {
        return franchiseOwnerService.findFranchiseOwnerById(franchiseOwnerCode);
    }

    @Operation(summary = "프랜차이즈 점주 등록", description = "신규 프랜차이즈 점주를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<String> registerFranchiseOwner(
            @RequestBody FranchiseOwnerDTO franchiseOwnerDTO
    ) {
        return franchiseOwnerService.registerFranchiseOwner(franchiseOwnerDTO);
    }

    @Operation(summary = "프랜차이즈 점주 정보 수정", description = "기존 프랜차이즈 점주의 정보를 수정합니다.")
    @PutMapping("/update/{franchiseOwnerCode}")
    public ResponseEntity<String> updateFranchiseOwner(
            @PathVariable int franchiseOwnerCode,
            @RequestBody FranchiseOwnerDTO updatedFranchiseOwner
    ) {
        return franchiseOwnerService.updateFranchiseOwner(franchiseOwnerCode, updatedFranchiseOwner);
    }

    @Operation(summary = "프랜차이즈 점주 삭제", description = "기존 프랜차이즈 점주를 삭제합니다.")
    @DeleteMapping("/delete/{franchiseOwnerCode}")
    public ResponseEntity<String> deleteFranchiseOwner(
            @PathVariable int franchiseOwnerCode
    ) {
        return franchiseOwnerService.deleteFranchiseOwner(franchiseOwnerCode);
    }

    @Operation(summary = "프랜차이즈 점주 비밀번호 초기화", description = "프랜차이즈 점주의 비밀번호를 초기화합니다.")
    @PostMapping("/reset-password/{franchiseOwnerCode}")
    public ResponseEntity<String> resetFranchiseOwnerPassword(
            @PathVariable int franchiseOwnerCode
    ) {
        return franchiseOwnerService.resetFranchiseOwnerPassword(franchiseOwnerCode);
    }
}
