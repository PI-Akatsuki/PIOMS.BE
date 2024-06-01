package com.akatsuki.pioms.admin.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private boolean isCurrentUserRoot() {
        String currentUsername = getCurrentUser();
        Admin requestorAdmin = adminRepository.findByAdminId(currentUsername).orElse(null);
        return requestorAdmin != null && "ROLE_ROOT".equals(requestorAdmin.getAdminRole());
    }

    private AdminDTO convertToDTO(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setAdminCode(admin.getAdminCode());
        adminDTO.setAdminName(admin.getAdminName());
        adminDTO.setAdminId(admin.getAdminId());
        adminDTO.setAdminPwd(admin.getAdminPwd());
        adminDTO.setEnrollDate(admin.getEnrollDate());
        adminDTO.setUpdateDate(admin.getUpdateDate());
        adminDTO.setDeleteDate(admin.getDeleteDate());
        adminDTO.setAdminEmail(admin.getAdminEmail());
        adminDTO.setAdminPhone(admin.getAdminPhone());
        adminDTO.setAccessNumber(admin.getAccessNumber());
        adminDTO.setAdminRole(admin.getAdminRole());
        adminDTO.setAdminStatus(admin.isAdminStatus());
        adminDTO.setFranchiseCodes(admin.getFranchise() != null ?
                admin.getFranchise().stream()
                        .map(Franchise::getFranchiseCode)
                        .collect(Collectors.toList())
                : null);
        adminDTO.setFranchiseCount(admin.getFranchiseCount());
        return adminDTO;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AdminDTO> findAdminList() {
        return adminRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<AdminDTO> findAdminById(int adminCode) {
        Admin admin = adminRepository.findById(adminCode).orElse(null);
        if (admin != null) {
            return ResponseEntity.ok(convertToDTO(admin));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> registerAdmin(AdminDTO adminDTO) {
        if (!isCurrentUserRoot()) {
            return ResponseEntity.status(403).body("신규 관리자 등록은 루트 관리자만 가능합니다.");
        }

        if (adminRepository.findByAdminId(adminDTO.getAdminId()).isPresent()) {
            return ResponseEntity.status(400).body("중복된 아이디가 존재합니다.");
        }

        if (adminDTO.getFranchiseCodes() != null && adminDTO.getFranchiseCodes().size() > 6) {
            return ResponseEntity.badRequest().body("관리자는 최대 6개의 가맹점만 등록할 수 있습니다.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);

        List<Franchise> franchises = adminDTO.getFranchiseCodes() != null ?
                adminDTO.getFranchiseCodes().stream()
                        .map(franchiseCode -> {
                            Franchise franchise = new Franchise();
                            franchise.setFranchiseCode(franchiseCode);
                            return franchise;
                        })
                        .collect(Collectors.toList())
                : Collections.emptyList();

        Admin admin = new Admin();
        admin.setAdminCode(adminDTO.getAdminCode());
        admin.setAdminName(adminDTO.getAdminName());
        admin.setAdminId(adminDTO.getAdminId());
        admin.setAdminPwd(passwordEncoder.encode(adminDTO.getAdminPwd()));
        admin.setEnrollDate(now);
        admin.setUpdateDate(now);
        admin.setAccessNumber(uuid);
        admin.setAdminStatus(true);
        admin.setAdminRole("ROLE_ADMIN");
        admin.setAdminEmail(adminDTO.getAdminEmail());
        admin.setAdminPhone(adminDTO.getAdminPhone());
        admin.setFranchise(franchises);

        adminRepository.save(admin);
        String username = getCurrentUser();
        logService.saveLog(username, LogStatus.등록, admin.getAdminName(), "Admin");
        return ResponseEntity.ok("신규 관리자 등록이 완료되었습니다.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateAdminInfo(int adminCode, AdminDTO updatedAdminDTO) {
        Admin admin = adminRepository.findById(adminCode).orElse(null);
        if (admin != null) {
            if (admin.getFranchise() != null && admin.getFranchise().size() > 6) {
                return ResponseEntity.badRequest().body("관리자는 최대 6개의 가맹점만 등록할 수 있습니다.");
            }

            StringBuilder changes = new StringBuilder();
            if (!admin.getAdminName().equals(updatedAdminDTO.getAdminName())) {
                admin.setAdminName(updatedAdminDTO.getAdminName());
                changes.append("Name: " + updatedAdminDTO.getAdminName() + "; ");
            }
            if (!passwordEncoder.matches(updatedAdminDTO.getAdminPwd(), admin.getAdminPwd())) {
                admin.setAdminPwd(passwordEncoder.encode(updatedAdminDTO.getAdminPwd()));
                changes.append("Pwd: Changed; ");
            }
            if (!admin.getAdminEmail().equals(updatedAdminDTO.getAdminEmail())) {
                admin.setAdminEmail(updatedAdminDTO.getAdminEmail());
                changes.append("Email: " + updatedAdminDTO.getAdminEmail() + "; ");
            }
            if (!admin.getAdminPhone().equals(updatedAdminDTO.getAdminPhone())) {
                admin.setAdminPhone(updatedAdminDTO.getAdminPhone());
                changes.append("Phone: " + updatedAdminDTO.getAdminPhone() + "; ");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            admin.setUpdateDate(LocalDateTime.now().format(formatter));

            adminRepository.save(admin);
            String username = getCurrentUser();
            if (changes.length() > 0) {
                logService.saveLog(username, LogStatus.수정, changes.toString(), "Admin");
            } else {
                logService.saveLog(username, LogStatus.수정, "변경사항 없음", "Admin");
            }
            return ResponseEntity.ok("관리자 정보 수정이 완료되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteAdmin(int adminCode) {
        if (!isCurrentUserRoot()) {
            return ResponseEntity.status(403).body("관리자 비활성화(삭제)는 루트 관리자만 가능합니다.");
        }

        if (adminCode == 1) {
            return ResponseEntity.badRequest().body("adminCode 1번은 비활성화(삭제)할 수 없습니다.");
        }

        Admin admin = adminRepository.findById(adminCode).orElse(null);
        if (admin != null) {
            if (!admin.isAdminStatus()) {
                return ResponseEntity.badRequest().body("이미 비활성화(삭제)된 관리자입니다.");
            }

            boolean hasFranchises = admin.getFranchise() != null && !admin.getFranchise().isEmpty();
            if (hasFranchises) {
                return ResponseEntity.badRequest().body("관리자가 관리하는 점포가 있어 비활성화(삭제)할 수 없습니다.");
            }

            admin.setDeleteDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            admin.setAdminStatus(false);

            adminRepository.save(admin);
            String username = getCurrentUser();
            logService.saveLog(username, LogStatus.삭제, admin.getAdminName(), "Admin");
            return ResponseEntity.ok("관리자 비활성화(삭제)가 완료되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
