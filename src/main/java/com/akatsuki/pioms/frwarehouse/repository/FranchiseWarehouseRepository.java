package com.akatsuki.pioms.frwarehouse.repository;


import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseWarehouseRepository extends JpaRepository<FranchiseWarehouse,Integer> {
    FranchiseWarehouse findByProductProductCodeAndFranchiseCode(int productCode, int franchiseCode);

    FranchiseWarehouse findByProductProductCode(int productCode);

}
