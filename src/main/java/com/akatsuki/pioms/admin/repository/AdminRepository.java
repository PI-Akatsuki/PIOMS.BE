package com.akatsuki.pioms.admin.repository;

import com.akatsuki.pioms.admin.aggregate.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByAdminId(String adminId);

    List<Admin> findAllByOrderByEnrollDateDesc();

    Admin findByAdminName(String userName);

    @Query("SELECT a FROM Admin a LEFT JOIN FETCH a.franchise f")
    List<Admin> findAllWithFranchiseNames();
}
