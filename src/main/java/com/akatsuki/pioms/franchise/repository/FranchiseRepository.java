package com.akatsuki.pioms.franchise.repository;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FranchiseRepository extends JpaRepository<Franchise, Integer> {
    Franchise findByFranchiseOwnerFranchiseOwnerCode(int franchiseOwnerCode);

    List<Franchise> findAllByDeliveryDriverDriverCode(int driverCode);

    List<Franchise> findAllByOrderByFranchiseEnrollDateDesc();

    List<Franchise> findAllByAdminAdminCode(int adminCode);

    List<Franchise> findByFranchiseName(String franchiseName);
}
