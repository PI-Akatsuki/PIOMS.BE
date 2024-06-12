package com.akatsuki.pioms.franchise.service;

import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface FranchiseService {

    // 프랜차이즈 전체 조회
    List<FranchiseDTO> findFranchiseList();

    // 프랜차이즈 상세 조회
    Optional<FranchiseDTO> findFranchiseById(int franchiseCode);

    // 프랜차이즈 등록
    ResponseEntity<String> saveFranchise(FranchiseDTO franchiseDTO);

    // 프랜차이즈 수정
    ResponseEntity<String> updateFranchise(int franchiseCode, FranchiseDTO updatedFranchiseDTO);

    // 프랜차이즈 삭제
    ResponseEntity<String> deleteFranchise(int franchiseCode);

    FranchiseDTO findFranchiseByFranchiseOwnerCode(int franchiseOwnerCode);

    List<FranchiseDTO> findFranchiseListByDriverCode(int driverCode);

    List<FranchiseDTO> findFranchiseByAdminCode();

    List<FranchiseDTO> findFranchiseListByUser(String userId);

    FranchiseDTO findFranchiseByFranchiseOwnerCode();
}
