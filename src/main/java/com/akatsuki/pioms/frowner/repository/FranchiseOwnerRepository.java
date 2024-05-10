package com.akatsuki.pioms.frowner.repository;

import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseOwnerRepository extends JpaRepository<FranchiseOwner, Integer> {
}
