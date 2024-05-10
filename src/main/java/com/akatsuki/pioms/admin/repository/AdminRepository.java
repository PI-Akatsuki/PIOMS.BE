package com.akatsuki.pioms.admin.repository;

import com.akatsuki.pioms.admin.aggregate.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}
