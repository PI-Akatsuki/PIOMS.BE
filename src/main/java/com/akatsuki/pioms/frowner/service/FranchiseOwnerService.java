package com.akatsuki.pioms.frowner.service;

import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FranchiseOwnerService {

    // 전체 조회
    List<FranchiseOwnerDTO> findAllFranchiseOwners();

    // 상세 조회
    ResponseEntity<FranchiseOwnerDTO> findFranchiseOwnerById(int franchiseOwnerCode);
}
