package com.akatsuki.pioms.franchise.controller;

import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.user.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/franchise")
@Tag(name = "[관리자]가맹점 정보 API")
public class AdminOnlyFranchiseController {

    private final FranchiseService franchiseService;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public AdminOnlyFranchiseController(FranchiseService franchiseService, CustomUserDetailsService customUserDetailsService) {
        this.franchiseService = franchiseService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Operation(summary = "프랜차이즈 전체 조회", description = "전체 프랜차이즈를 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<FranchiseDTO>> getFranchiseList(Authentication authentication) {
        String userRole = customUserDetailsService.getUserRole(authentication);

        List<FranchiseDTO> franchiseList;
        if ("ROLE_ROOT".equals(userRole)) {
            franchiseList = franchiseService.findFranchiseList();
        } else {
            String userId = authentication.getName();
            franchiseList = franchiseService.findFranchiseByAdminCode();
        }

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
            @RequestBody FranchiseDTO updatedFranchiseDTO
    ) {
        return franchiseService.updateFranchise(franchiseCode, updatedFranchiseDTO);
    }

    @Operation(summary = "프랜차이즈 삭제", description = "기존 프랜차이즈를 삭제합니다.")
    @DeleteMapping("/delete/{franchiseCode}")
    public ResponseEntity<String> deleteFranchise(@PathVariable int franchiseCode) {
        return franchiseService.deleteFranchise(franchiseCode);
    }
}
