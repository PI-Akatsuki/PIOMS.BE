package com.akatsuki.pioms.ask.repository;


import com.akatsuki.pioms.ask.aggregate.FranchiseOwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseOwnerRepository extends JpaRepository<FranchiseOwnerEntity, Integer> {
}
