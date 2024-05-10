package com.akatsuki.pioms.franchise.repository;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseRepository extends JpaRepository<Franchise, Integer> {
}