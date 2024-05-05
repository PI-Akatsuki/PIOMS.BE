package com.akatsuki.pioms.specs.repository;

import com.akatsuki.pioms.specs.entity.SpecsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecsRepository extends JpaRepository<SpecsEntity,Integer> {

}
