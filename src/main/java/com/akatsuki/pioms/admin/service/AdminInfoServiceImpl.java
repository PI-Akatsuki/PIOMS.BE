package com.akatsuki.pioms.admin.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.repository.FranchiseRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminInfoServiceImpl implements AdminInfoService {
    private final AdminRepository adminRepository;
    private final FranchiseRepository franchiseRepository;
    private final LogService logService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInfoServiceImpl(AdminRepository adminRepository, FranchiseRepository franchiseRepository, LogService logService, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.franchiseRepository = franchiseRepository;
        this.logService = logService;
        this.passwordEncoder = passwordEncoder;
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private boolean isCurrentUserRoot() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if ("ROLE_ROOT".equals(authority.getAuthority())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AdminDTO> findAdminList() {
        return adminRepository.findAllByOrderByEnrollDateDesc().stream()
                .map(AdminDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<AdminDTO> findAdminById(int adminCode) {
        Admin admin = adminRepository.findById(adminCode).orElse(null);
        if (admin != null) {
            return ResponseEntity.ok(new AdminDTO(admin));
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

        if (adminDTO.getFranchiseNames() != null && adminDTO.getFranchiseNames().size() > 6) {
            return ResponseEntity.badRequest().body("관리자는 최대 6개의 가맹점만 등록할 수 있습니다.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);

        Admin admin = Admin.builder()
                .adminCode(adminDTO.getAdminCode())
                .adminName(adminDTO.getAdminName())
                .adminId(adminDTO.getAdminId())
                .adminPwd(passwordEncoder.encode(adminDTO.getAdminPwd()))
                .enrollDate(now)
                .updateDate(now)
                .accessNumber(uuid)
                .adminStatus(true)
                .adminRole("ROLE_ADMIN")
                .adminEmail(adminDTO.getAdminEmail())
                .adminPhone(adminDTO.getAdminPhone())
                .build();

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
            if (admin.getFranchise() != null && admin.getFranchise().size() > 3) {
                return ResponseEntity.badRequest().body("관리자는 최대 3개의 가맹점만 등록할 수 있습니다.");
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
            if (admin.isAdminStatus() != updatedAdminDTO.isAdminStatus()) {
                admin.setAdminStatus(updatedAdminDTO.isAdminStatus());
                changes.append("Status: " + (updatedAdminDTO.isAdminStatus() ? "활성화" : "비활성화") + "; ");
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

    @Override
    @Transactional
    public ResponseEntity<String> resetAdminPassword(int adminCode) {
        if (!isCurrentUserRoot()) {
            return ResponseEntity.status(403).body("비밀번호 초기화는 루트 관리자만 가능합니다.");
        }

        Admin admin = adminRepository.findById(adminCode)
                .orElseThrow(() -> new RuntimeException("관리자 코드를 찾을 수 없음: " + adminCode));

        String encodedPassword = passwordEncoder.encode("1234");
        admin.setAdminPwd(encodedPassword);
        adminRepository.save(admin);

        String username = getCurrentUser();
        logService.saveLog(username, LogStatus.수정, "비밀번호 초기화: " + admin.getAdminName(), "Admin");

        return ResponseEntity.ok("관리자 비밀번호 초기화가 완료되었습니다.");
    }


    @Override
    @Transactional
    public ResponseEntity<String> addFranchise(int adminCode, int franchiseCode) {
        Admin admin = adminRepository.findById(adminCode).orElse(null);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }

        if (admin.getFranchise() != null && admin.getFranchise().size() >= 6) {
            return ResponseEntity.badRequest().body("관리자는 최대 6개의 가맹점만 등록할 수 있습니다.");
        }

        Franchise franchise = franchiseRepository.findById(franchiseCode)
                .orElseThrow(() -> new RuntimeException("가맹점을 찾을 수 없음: " + franchiseCode));

        if (franchise.getAdmin() != null && franchise.getAdmin().getAdminCode() == adminCode) {
            return ResponseEntity.badRequest().body("중복된 가맹점이 존재합니다: " + franchise.getFranchiseName());
        }

        franchise.setAdmin(admin);
        franchiseRepository.save(franchise);

        return ResponseEntity.ok("가맹점 추가가 완료되었습니다.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> removeFranchise(int adminCode, int franchiseCode) {
        Admin admin = adminRepository.findById(adminCode).orElse(null);
        if (admin == null) {
            return ResponseEntity.notFound().build();
        }

        Franchise franchise = franchiseRepository.findById(franchiseCode)
                .orElseThrow(() -> new RuntimeException("가맹점을 찾을 수 없음: " + franchiseCode));

        if (franchise.getAdmin() == null || franchise.getAdmin().getAdminCode() != adminCode) {
            return ResponseEntity.badRequest().body("해당 가맹점이 관리자에 존재하지 않습니다: " + franchise.getFranchiseName());
        }

        franchise.setAdmin(null);
        franchiseRepository.save(franchise);

        return ResponseEntity.ok("가맹점 제거가 완료되었습니다.");
    }

}
