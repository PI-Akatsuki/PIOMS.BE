package com.akatsuki.pioms.admin.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

    private AdminDTO convertToDTO(Admin admin) {
        return AdminDTO.builder()
                .adminCode(admin.getAdminCode())
                .adminName(admin.getAdminName())
                .adminId(admin.getAdminId())
                .adminPwd(admin.getAdminPwd())
                .enrollDate(admin.getEnrollDate())
                .updateDate(admin.getUpdateDate())
                .deleteDate(admin.getDeleteDate())
                .adminEmail(admin.getAdminEmail())
                .adminPhone(admin.getAdminPhone())
                .accessNumber(admin.getAccessNumber())
                .adminRole(admin.getAdminRole())
                .adminStatus(admin.isAdminStatus())
                .franchiseList(admin.getFranchise() != null ?
                        admin.getFranchise().stream()
                                .map(franchise -> new FranchiseDTO(franchise))
                                .collect(Collectors.toList())
                        : null)
                .build();
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
        String currentUsername = getCurrentUser();
        Admin requestorAdmin = adminRepository.findByAdminId(currentUsername).orElse(null);
        if (requestorAdmin == null || !requestorAdmin.getAdminRole().equals("ROLE_ROOT")) {
            return ResponseEntity.status(403).body("신규 관리자 등록은 루트 관리자만 가능합니다.");
        }

        if (adminRepository.findByAdminId(adminDTO.getAdminId()).isPresent()) {
            return ResponseEntity.status(400).body("중복된 아이디가 존재합니다.");
        }

        if (adminDTO.getFranchiseList() != null && adminDTO.getFranchiseList().size() > 6) {
            return ResponseEntity.badRequest().body("관리자는 최대 6개의 가맹점만 등록할 수 있습니다.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String now = LocalDateTime.now().format(formatter);

        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);

        List<Franchise> franchises = adminDTO.getFranchiseList() != null ?
                adminDTO.getFranchiseList().stream()
                        .map(franchiseDTO -> Franchise.builder()
                                .franchiseCode(franchiseDTO.getFranchiseCode())
                                .franchiseName(franchiseDTO.getFranchiseName())
                                .franchiseAddress(franchiseDTO.getFranchiseAddress())
                                .franchiseCall(franchiseDTO.getFranchiseCall())
                                .franchiseEnrollDate(franchiseDTO.getFranchiseEnrollDate())
                                .franchiseUpdateDate(franchiseDTO.getFranchiseUpdateDate())
                                .franchiseDeleteDate(franchiseDTO.getFranchiseDeleteDate())
                                .franchiseBusinessNum(franchiseDTO.getFranchiseBusinessNum())
                                .franchiseDeliveryDate(franchiseDTO.getFranchiseDeliveryDate())
                                .franchiseOwner(franchiseDTO.getFranchiseOwner().toEntity())
                                .admin(requestorAdmin)
                                .build())
                        .collect(Collectors.toList())
                : Collections.emptyList();

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
                .franchise(franchises)
                .build();

        adminRepository.save(admin);
        logService.saveLog("root", LogStatus.등록, admin.getAdminName(), "Admin");
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

            Admin.AdminBuilder adminBuilder = Admin.builder()
                    .adminCode(admin.getAdminCode())
                    .adminId(admin.getAdminId())
                    .adminRole(admin.getAdminRole())
                    .adminStatus(admin.isAdminStatus())
                    .enrollDate(admin.getEnrollDate())
                    .deleteDate(admin.getDeleteDate());

            StringBuilder changes = new StringBuilder();
            if (!admin.getAdminName().equals(updatedAdminDTO.getAdminName())) {
                adminBuilder.adminName(updatedAdminDTO.getAdminName());
                changes.append("Name: " + updatedAdminDTO.getAdminName() + "; ");
            }
            if (!passwordEncoder.matches(updatedAdminDTO.getAdminPwd(), admin.getAdminPwd())) {
                adminBuilder.adminPwd(passwordEncoder.encode(updatedAdminDTO.getAdminPwd()));
                changes.append("Pwd: Changed; ");
            }
            if (!admin.getAdminEmail().equals(updatedAdminDTO.getAdminEmail())) {
                adminBuilder.adminEmail(updatedAdminDTO.getAdminEmail());
                changes.append("Email: " + updatedAdminDTO.getAdminEmail() + "; ");
            }
            if (!admin.getAdminPhone().equals(updatedAdminDTO.getAdminPhone())) {
                adminBuilder.adminPhone(updatedAdminDTO.getAdminPhone());
                changes.append("Phone: " + updatedAdminDTO.getAdminPhone() + "; ");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            adminBuilder.updateDate(LocalDateTime.now().format(formatter));

            Admin updatedAdmin = adminBuilder.build();
            adminRepository.save(updatedAdmin);
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
    public ResponseEntity<String> deleteAdmin(int adminCode) {
        String currentUsername = getCurrentUser();
        Admin requestorAdmin = adminRepository.findByAdminId(currentUsername).orElse(null);
        if (adminCode == 1) {
            return ResponseEntity.badRequest().body("adminCode 1번은 비활성화(삭제)할 수 없습니다.");
        }

        if (requestorAdmin == null || !requestorAdmin.getAdminRole().equals("ROLE_ROOT")) {
            return ResponseEntity.status(403).body("관리자 비활성화(삭제)는 루트 관리자만 가능합니다.");
        }

        Admin admin = adminRepository.findById(adminCode).orElse(null);
        if (admin != null) {
            boolean hasFranchises = admin.getFranchise() != null && !admin.getFranchise().isEmpty();

            if (hasFranchises) {
                return ResponseEntity.badRequest().body("관리자가 관리하는 점포가 있어 비활성화(삭제)할 수 없습니다.");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);

            Admin updatedAdmin = Admin.builder()
                    .adminCode(admin.getAdminCode())
                    .adminName(admin.getAdminName())
                    .adminId(admin.getAdminId())
                    .adminPwd(admin.getAdminPwd())
                    .enrollDate(admin.getEnrollDate())
                    .updateDate(admin.getUpdateDate())
                    .deleteDate(formattedDateTime)
                    .adminEmail(admin.getAdminEmail())
                    .adminPhone(admin.getAdminPhone())
                    .accessNumber(admin.getAccessNumber())
                    .adminRole(admin.getAdminRole())
                    .adminStatus(false)
                    .build();

            adminRepository.save(updatedAdmin);
            logService.saveLog("root", LogStatus.삭제, admin.getAdminName(), "Admin");
            return ResponseEntity.ok("관리자 비활성화(삭제)가 완료됨.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
