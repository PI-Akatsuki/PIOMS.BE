package com.akatsuki.pioms.frowner.controller;

import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwnerVO;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.service.FranchiseOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/franchise/owner")
public class AdminOnlyFrOwnerController {

    private final FranchiseOwnerService franchiseOwnerService;

    @Autowired
    public AdminOnlyFrOwnerController(FranchiseOwnerService franchiseOwnerService) {
        this.franchiseOwnerService = franchiseOwnerService;
    }


    // 전체 조회
    @Operation(summary = "점주 전체 조회", description = "점주 전체 목록을 조회하며, 담당 점포와 관리자도 함께 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<List<FranchiseOwnerVO>> getOwnerListWithFranchiseAndAdmin() {
        List<FranchiseOwnerVO> ownerList = franchiseOwnerService.findAllFranchiseOwners();
        return ResponseEntity.ok(ownerList);
    }

    // 상세 조회
    @Operation(summary = "점주의 점포 조회", description = "점주가 담당하는 점포와 관리자를 조회합니다.")
    @GetMapping("/detail/{ownerCode}")
    public ResponseEntity<FranchiseOwnerVO> getOwnerWithFranchiseAndAdmin(@PathVariable int ownerCode) {
        Optional<FranchiseOwnerVO> owner = franchiseOwnerService.findFranchiseOwnerById(ownerCode);
        return owner.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
