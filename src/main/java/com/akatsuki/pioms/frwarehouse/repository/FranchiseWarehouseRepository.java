package com.akatsuki.pioms.frwarehouse.repository;


import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FranchiseWarehouseRepository extends JpaRepository<FranchiseWarehouse,Integer> {
    FranchiseWarehouse findByProductProductCodeAndFranchiseCode(int productCode, int franchiseCode);

    FranchiseWarehouse findByProductProductCode(int productCode);

    List<FranchiseWarehouse> findByFranchiseWarehouseFavoriteTrue();

    List<FranchiseWarehouse> findAllByFranchiseCode(int franchiseCode);

}
