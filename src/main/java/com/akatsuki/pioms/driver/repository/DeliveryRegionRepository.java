package com.akatsuki.pioms.driver.repository;

import com.akatsuki.pioms.driver.aggregate.DeliveryRegion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRegionRepository extends JpaRepository<DeliveryRegion,Integer> {


    DeliveryRegion findByFranchiseFranchiseCode(int franchiseCode);
}
