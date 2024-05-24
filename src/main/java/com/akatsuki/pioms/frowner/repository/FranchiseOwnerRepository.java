package com.akatsuki.pioms.frowner.repository;

import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranchiseOwnerRepository extends JpaRepository<FranchiseOwner, Integer> {
    boolean existsByFranchiseOwnerId(String franchiseOwnerId);

    Optional<FranchiseOwner> findByFranchiseOwnerId(String frOwnerId);

    FranchiseOwner findByFranchiseOwnerCode(int franchiseOwnerCode);
}
