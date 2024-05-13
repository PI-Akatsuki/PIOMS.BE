package com.akatsuki.pioms.frowner.controller;

import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.service.FranchiseOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("franchise/mypage")
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
}
