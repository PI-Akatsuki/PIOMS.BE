package com.akatsuki.pioms.frowner.repository;

import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FranchiseOwnerRepository extends JpaRepository<FranchiseOwner, Integer> {
    boolean existsByFranchiseOwnerId(String franchiseOwnerId);

    Optional<FranchiseOwner> findByFranchiseOwnerId(String frOwnerId);

    FranchiseOwner findByFranchiseOwnerCode(int franchiseOwnerCode);

    FranchiseOwner findByFranchiseOwnerName(String userName);

    // 루트 전체 조회 DESC
    List<FranchiseOwner> findAllByOrderByFranchiseOwnerEnrollDateDesc();

    // !루트 전체 조회 DESC
    List<FranchiseOwner> findAllByFranchiseAdminAdminCodeOrderByFranchiseOwnerEnrollDateDesc(int adminCode);

    List<FranchiseOwner> findAllByFranchiseAdminAdminCode(int adminCode);
}
