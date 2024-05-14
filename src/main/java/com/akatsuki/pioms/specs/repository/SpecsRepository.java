package com.akatsuki.pioms.specs.repository;

import com.akatsuki.pioms.specs.aggregate.SpecsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecsRepository extends JpaRepository<SpecsEntity,Integer> {
    List<SpecsEntity> findAllByOrderFranchiseFranchiseCode(int franchiseCode);

//    List<SpecsEntity> findAllAndOrderBySpecsDateDesc();

}
