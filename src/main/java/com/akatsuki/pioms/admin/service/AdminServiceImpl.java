package com.akatsuki.pioms.admin.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Admin> findAdminList() {
        return adminRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Admin> findAdminById(int adminCode) {
        return adminRepository.findById(adminCode);
    }
}
