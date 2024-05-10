package com.akatsuki.pioms.franchise.controller;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "프랜차이즈 마이페이지", description = "Franchise MyPage")
@RestController
@RequestMapping("franchise/mypage")
public class FranchiseOnlyFranchiseController {

    private final FranchiseService franchiseService;

    @Autowired
    public FranchiseOnlyFranchiseController(FranchiseService franchiseService) {
        this.franchiseService = franchiseService;
    }

    // 내가맹 상세 조회
    @Operation(summary = "내가맹 상세 조회", description = "나의 가맹정보를 상세 조회합니다.")
    @GetMapping("/detail/{franchiseCode}")
    public ResponseEntity<Franchise> getMyFranchiseById(
            @PathVariable int franchiseCode,
            @RequestParam int requestorOwnerCode
    ) {
        Optional<Franchise> franchiseOptional = franchiseService.findFranchiseById(franchiseCode);

        if (franchiseOptional.isPresent()) {
            Franchise franchise = franchiseOptional.get();
            // 프랜차이즈 점주 확인
            if (franchise.getFranchiseOwner().getFranchiseOwnerCode() == requestorOwnerCode) {
                return ResponseEntity.ok(franchise);
            } else {
                // 접근 권한 없음
                return ResponseEntity.status(403).body(null);
            }
        } else {
            // 프랜차이즈를 찾을 수 없음
            return ResponseEntity.notFound().build();
        }
    }

    // 내가맹 수정
    @Operation(summary = "내가맹 수정", description = "내가맹 정보를 수정합니다.")
    @PutMapping("/update")
    public ResponseEntity<String> updateMyFranchise(
            @RequestParam int requestorOwnerCode,
            @RequestBody Franchise updatedFranchise) {
        Optional<Franchise> franchiseOptional = franchiseService.findFranchiseById(updatedFranchise.getFranchiseCode());

        if (franchiseOptional.isPresent()) {
            Franchise franchise = franchiseOptional.get();
            // 프랜차이즈 소유주 코드 확인
            if (franchise.getFranchiseOwner().getFranchiseOwnerCode() == requestorOwnerCode) {
                return franchiseService.updateFranchise(updatedFranchise.getFranchiseCode(), updatedFranchise, requestorOwnerCode, true);
            } else {
                return ResponseEntity.status(403).body("수정 권한이 없습니다."); // 접근 권한 없음
            }
        } else {
            return ResponseEntity.notFound().build(); // 프랜차이즈를 찾을 수 없음
        }
    }
}
