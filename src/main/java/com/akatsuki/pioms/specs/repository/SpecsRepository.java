package com.akatsuki.pioms.specs.repository;

import com.akatsuki.pioms.specs.aggregate.Specs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecsRepository extends JpaRepository<Specs,Integer> {
    List<Specs> findAllByOrderFranchiseFranchiseCode(int franchiseCode);

    List<Specs> findAllByOrderFranchiseAdminAdminCode(int adminCode);

    Specs findByOrderOrderCode(int orderCode);

    List<Specs> findAllByOrderFranchiseFranchiseOwnerFranchiseOwnerCode(int franchiseCode);

}
