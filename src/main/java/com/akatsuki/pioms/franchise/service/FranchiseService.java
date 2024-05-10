package com.akatsuki.pioms.franchise.service;

import com.akatsuki.pioms.franchise.aggregate.Franchise;

import java.util.List;
import java.util.Optional;

public interface FranchiseService {

    // 프랜차이즈 전체 조회
    List<Franchise> findFranchiseList();

    Optional<Franchise> findFranchiseById(int franchiseCode);

}
