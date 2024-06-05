package com.akatsuki.pioms.frowner.service;

import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FranchiseOwnerService {

    // 전체 조회
    List<FranchiseOwnerDTO> findAllFranchiseOwners();

    // 상세 조회
    ResponseEntity<FranchiseOwnerDTO> findFranchiseOwnerById(int franchiseOwnerCode);

    // 오너 등록
    ResponseEntity<String> registerFranchiseOwner(FranchiseOwnerDTO franchiseOwnerDTO);

    // 오너 수정
    ResponseEntity<String> updateFranchiseOwner(int franchiseOwnerCode, FranchiseOwnerDTO updatedFranchiseOwnerDTO);

    // 오너 삭제
    ResponseEntity<String> deleteFranchiseOwner(int franchiseOwnerCode);

    // 비밀번호 초기화
    ResponseEntity<String> resetFranchiseOwnerPassword(int franchiseOwnerCode);

    FranchiseOwnerDTO getFranchiseOwnerWithFranchiseName(int franchiseOwnerCode);

}
