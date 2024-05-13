package com.akatsuki.pioms.frowner.service;

import com.akatsuki.pioms.frowner.aggregate.FranchiseOwnerVO;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;

import java.util.List;
import java.util.Optional;

public interface FranchiseOwnerService {

    // 전체 조회
    List<FranchiseOwnerVO> findAllFranchiseOwners();

    // 상세 조회
    Optional<FranchiseOwnerVO> findFranchiseOwnerById(int franchiseOwnerCode);
}
