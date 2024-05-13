package com.akatsuki.pioms.frowner.controller;

import com.akatsuki.pioms.frowner.aggregate.FranchiseOwnerVO;
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
    @Operation(summary = "점주의 점포 조회", description = "점주가 담당하는 점포와 관리자를 조회합니다.")
    @GetMapping("info/{ownerCode}")
    public ResponseEntity<FranchiseOwnerVO> getOwnerWithFranchiseAndAdmin(@PathVariable int ownerCode) {
        Optional<FranchiseOwnerVO> owner = franchiseOwnerService.findFranchiseOwnerById(ownerCode);
        return owner.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
