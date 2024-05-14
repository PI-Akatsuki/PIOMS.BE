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

    // 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<Admin> findAdminList() {
        return adminRepository.findAll();
    }



    // 상세 조회
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


    // 신규 관리자 등록
    @Override
    @Transactional
    public ResponseEntity<String> saveAdmin(Admin admin, int requestorAdminCode) {

        // 루트 관리자 확인
        Optional<Admin> requestorAdmin = adminRepository.findById(requestorAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("신규 관리자 등록은 루트 관리자만 가능합니다.");
        }

        // 등록된 가맹점 수 확인
        if (admin.getFranchise() != null && admin.getFranchise().size() > 6) {
            return ResponseEntity.badRequest().body("관리자는 최대 6개의 가맹점만 등록할 수 있습니다.");
        }

        // 날짜 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        // 등록일
        admin.setEnrollDate(now);

        // 수정일
        admin.setUpdateDate(now);

        // 관리자 엑세스번호 발급
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        admin.setAccessNumber(uuid);
        admin.setAdminStatus(true);

        adminRepository.save(admin);
        logService.saveLog("root", LogStatus.등록, admin.getAdminName(), "Admin");
        return ResponseEntity.ok("신규 관리자 등록이 완료되었습니다.");
    }

    // 관리자 정보 수정
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
            if (!admin.getAdminPwd().equals(updatedAdmin.getAdminPwd())) {
                changes.append("Pwd: Changed; ");
            }
            if (!admin.getAdminEmail().equals(updatedAdmin.getAdminEmail())) {
                changes.append("Email: " + updatedAdmin.getAdminEmail() + "; ");
            }
            if (!admin.getAdminPhone().equals(updatedAdmin.getAdminPhone())) {
                changes.append("Phone: " + updatedAdmin.getAdminPhone() + "; ");
            }

            // 정보 수정
            admin.setAdminName(updatedAdmin.getAdminName());
            admin.setAdminPwd(updatedAdmin.getAdminPwd());
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
    // 비활성화
    @Override
    @Transactional
    public ResponseEntity<String> deleteAdmin(int adminCode, int requestorAdminCode) {
        // adminCode가 1인 경우 비활성화 방지
        if (adminCode == 1) {
            return ResponseEntity.badRequest().body("adminCode 1번은 비활성화(삭제)할 수 없습니다.");
        }

        // 루트 관리자 확인
        Optional<Admin> requestorAdmin = adminRepository.findById(requestorAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("관리자 비활성화(삭제)는 루트 관리자만 가능합니다.");
        }

        Optional<Admin> adminOptional = adminRepository.findById(adminCode);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();

            // 관리자가 관리하는 점포가 있는지 확인
            boolean hasFranchises = admin.getFranchise() != null && !admin.getFranchise().isEmpty();

            if (hasFranchises) {
                return ResponseEntity.badRequest().body("관리자가 관리하는 점포가 있어 비활성화(삭제)할 수 없습니다.");
            }

            // 현재 시간 포맷 설정 (년-월-일 시:분:초)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);

            // 점포가 없는 경우 비활성화(삭제)
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
