package com.akatsuki.pioms.ask.repository;

import com.akatsuki.pioms.ask.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
}
