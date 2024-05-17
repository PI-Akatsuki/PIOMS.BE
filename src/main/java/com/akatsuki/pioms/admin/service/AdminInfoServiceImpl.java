package com.akatsuki.pioms.admin.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminInfoServiceImpl implements AdminInfoService {
    private final AdminRepository adminRepository;
    private final LogService logService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInfoServiceImpl(AdminRepository adminRepository, LogService logService, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.logService = logService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Admin> findAdminList() {
        return adminRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<Admin> findAdminById(int adminCode) {
        Admin admin = adminRepository.findById(adminCode).orElse(null);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> saveAdmin(Admin admin, int requestorAdminCode) {
        Optional<Admin> requestorAdmin = adminRepository.findById(requestorAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("신규 관리자 등록은 루트 관리자만 가능합니다.");
        }

        if (admin.getFranchise() != null && admin.getFranchise().size() > 6) {
            return ResponseEntity.badRequest().body("관리자는 최대 6개의 가맹점만 등록할 수 있습니다.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        admin.setEnrollDate(now);
        admin.setUpdateDate(now);

        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        admin.setAccessNumber(uuid);
        admin.setAdminStatus(true);
        admin.setAdminPwd(passwordEncoder.encode(admin.getAdminPwd()));

        adminRepository.save(admin);
        logService.saveLog("root", LogStatus.등록, admin.getAdminName(), "Admin");
        return ResponseEntity.ok("신규 관리자 등록이 완료되었습니다.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateAdminInfo(int adminCode, Admin updatedAdmin) {
        Optional<Admin> adminOptional = adminRepository.findById(adminCode);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            if (admin.getFranchise() != null && admin.getFranchise().size() > 6) {
                return ResponseEntity.badRequest().body("관리자는 최대 6개의 가맹점만 등록할 수 있습니다.");
            }

            StringBuilder changes = new StringBuilder();
            if (!admin.getAdminName().equals(updatedAdmin.getAdminName())) {
                changes.append("Name: " + updatedAdmin.getAdminName() + "; ");
            }
            if (!passwordEncoder.matches(updatedAdmin.getAdminPwd(), admin.getAdminPwd())) {
                admin.setAdminPwd(passwordEncoder.encode(updatedAdmin.getAdminPwd()));
                changes.append("Pwd: Changed; ");
            }
            if (!admin.getAdminEmail().equals(updatedAdmin.getAdminEmail())) {
                changes.append("Email: " + updatedAdmin.getAdminEmail() + "; ");
            }
            if (!admin.getAdminPhone().equals(updatedAdmin.getAdminPhone())) {
                changes.append("Phone: " + updatedAdmin.getAdminPhone() + "; ");
            }

            admin.setAdminName(updatedAdmin.getAdminName());
            admin.setAdminEmail(updatedAdmin.getAdminEmail());
            admin.setAdminPhone(updatedAdmin.getAdminPhone());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            admin.setUpdateDate(LocalDateTime.now().format(formatter));

            adminRepository.save(admin);
            if (changes.length() > 0) {
                logService.saveLog("root", LogStatus.수정, changes.toString(), "Admin");
            } else {
                logService.saveLog("root", LogStatus.수정, "No changes", "Admin");
            }
            return ResponseEntity.ok("관리자 정보 수정이 완료되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteAdmin(int adminCode, int requestorAdminCode) {
        if (adminCode == 1) {
            return ResponseEntity.badRequest().body("adminCode 1번은 비활성화(삭제)할 수 없습니다.");
        }

        Optional<Admin> requestorAdmin = adminRepository.findById(requestorAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("관리자 비활성화(삭제)는 루트 관리자만 가능합니다.");
        }

        Optional<Admin> adminOptional = adminRepository.findById(adminCode);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            boolean hasFranchises = admin.getFranchise() != null && !admin.getFranchise().isEmpty();

            if (hasFranchises) {
                return ResponseEntity.badRequest().body("관리자가 관리하는 점포가 있어 비활성화(삭제)할 수 없습니다.");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);

            admin.setAdminStatus(false);
            admin.setDeleteDate(formattedDateTime);
            adminRepository.save(admin);
            logService.saveLog("root", LogStatus.삭제, admin.getAdminName(), "Admin");
            return ResponseEntity.ok("관리자 비활성화(삭제)가 완료됨.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
