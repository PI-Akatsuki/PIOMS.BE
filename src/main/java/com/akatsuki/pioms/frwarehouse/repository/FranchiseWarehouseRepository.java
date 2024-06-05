package com.akatsuki.pioms.frwarehouse.repository;


import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
import com.akatsuki.pioms.product.aggregate.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FranchiseWarehouseRepository extends JpaRepository<FranchiseWarehouse,Integer> {
    FranchiseWarehouse findByProductProductCodeAndFranchiseCode(int productCode, int franchiseCode);

    List<FranchiseWarehouse> findAllByFranchiseCode(int franchiseCode);

    List<FranchiseWarehouse> findByFranchiseWarehouseCode(int franchiseWarehouseCode);

    List<FranchiseWarehouse> findByFranchiseWarehouseFavoriteTrue();

    List<FranchiseWarehouse> findByFranchiseCode(int franchiseCode);

    List<FranchiseWarehouse> findByFranchiseCodeAndFranchiseWarehouseFavorite(int franchiseCode, boolean favorite);

}
