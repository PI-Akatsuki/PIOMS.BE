package com.akatsuki.pioms.franchiseWarehouse.repository;


import com.akatsuki.pioms.franchiseWarehouse.aggregate.FranchiseWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FranchiseWarehouseRepository extends JpaRepository<FranchiseWarehouse,Integer> {
    FranchiseWarehouse findByProductProductCodeAndFranchiseCode(int productCode, int franchiseCode);
    List<FranchiseWarehouse> findByFranchiseWarehouseFavoriteTrue();
}
