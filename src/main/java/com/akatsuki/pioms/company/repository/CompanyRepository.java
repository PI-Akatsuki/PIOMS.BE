package com.akatsuki.pioms.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.akatsuki.pioms.company.entity.CompanyEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {
}

