package com.akatsuki.pioms.franchise.repository;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FranchiseRepository extends JpaRepository<Franchise, Integer> {
    Franchise findByFranchiseOwnerFranchiseOwnerCode(int franchiseOwnerCode);

    List<Franchise> findAllByDeliveryDriverDriverCode(int driverCode);

    List<Franchise> findAllByAdminAdminCode(int adminCode);
}
