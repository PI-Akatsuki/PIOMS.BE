package com.akatsuki.pioms.admin.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<Admin> findAdminList();

    Optional<Admin> findAdminById(int adminCode);

    ResponseEntity<String> deleteAdmin(int adminCode);

}
