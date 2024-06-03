package com.akatsuki.pioms.franchise.service;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface FranchiseService {

    // 프랜차이즈 전체 조회
    List<Franchise> findFranchiseList();

    // 프랜차이즈 상세 조회
    Optional<Franchise> findFranchiseById(int franchiseCode);

    // 프랜차이즈 등록
    ResponseEntity<String> saveFranchise(Franchise franchise, int requestorAdminCode);

    // 프랜차이즈 수정
    ResponseEntity<String> updateFranchise(int franchiseCode, Franchise updatedFranchise, int requestorCode, boolean isOwner);

    // 프랜차이즈 삭제
    ResponseEntity<String> deleteFranchise(int franchiseCode, int requestorAdminCode);

    FranchiseDTO findFranchiseByFranchiseOwnerCode(int franchiseOwnerCode);


    List<FranchiseDTO> findFranchiseListByDriverCode(int driverCode);

    List<Franchise> findFranchiseByAdminCode(int adminCode);
}
