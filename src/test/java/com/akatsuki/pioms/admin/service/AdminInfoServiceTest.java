//package com.akatsuki.pioms.admin.service;
//
//import com.akatsuki.pioms.admin.aggregate.Admin;
//import com.akatsuki.pioms.admin.dto.AdminDTO;
//import com.akatsuki.pioms.admin.repository.AdminRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class AdminInfoServiceTest {
//
//    @Autowired
//    private AdminInfoService adminInfoService;
//
//    @Autowired
//    private AdminRepository adminRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private Admin admin;
//
//    @BeforeEach
//    public void setup() {
//        admin = new Admin();
//        admin.setAdminCode(1);
//        admin.setAdminId("root");
//        admin.setAdminPwd(passwordEncoder.encode("root")); // 암호화 설정
//        admin.setAccessNumber("rootAccess");
//        admin.setAdminStatus(true);
//        admin.setAdminName("root");
//        admin.setAdminEmail("root@example.com");
//        admin.setAdminPhone("010-1234-5678");
//        admin.setEnrollDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        admin.setUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        admin.setAdminRole("ROLE_ADMIN");
//        admin.setFranchise(new ArrayList<>());
//
//        adminRepository.save(admin);
//    }
//
//    @Test
//    @Transactional
//    public void 관리자_전체조회() {
//        // When
//        List<AdminDTO> adminList = adminInfoService.findAdminList();
//
//        // Then
//        assertNotNull(adminList);
//        assertTrue(adminList.size() > 0);
//    }
//
//    @Test
//    @Transactional
//    public void 관리자_상세조회() {
//        // When
//        ResponseEntity<AdminDTO> response = adminInfoService.findAdminById(admin.getAdminCode());
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertEquals(admin.getAdminId(), response.getBody().getAdminId());
//    }
//
//    @Test
//    @Transactional
//    public void 관리자_등록() {
//        // Given
//        AdminDTO newAdminDTO = AdminDTO.builder()
//                .adminId("newAdminId")
//                .adminName("newAdmin")
//                .adminPwd("newPassword")
//                .adminEmail("newAdmin@example.com")
//                .adminPhone("010-5678-1234")
//                .franchiseList(new ArrayList<>())
//                .build();
//
//        // When
//        ResponseEntity<String> response = adminInfoService.registerAdmin(newAdminDTO, admin.getAdminCode());
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("신규 관리자 등록이 완료되었습니다.", response.getBody());
//
//        // 등록된 관리자 확인
//        List<AdminDTO> adminList = adminInfoService.findAdminList();
//        assertTrue(adminList.stream().anyMatch(a -> a.getAdminId().equals("newAdminId")));
//    }
//
//    @Test
//    @Transactional
//    public void 관리자_등록_권한없음() {
//        // Given
//        Admin requestorAdmin = new Admin();
//        requestorAdmin.setAdminCode(2); // 루트 관리자가 아님
//        requestorAdmin.setAdminId("requestorAdminId");
//        adminRepository.save(requestorAdmin);
//
//        AdminDTO newAdminDTO = AdminDTO.builder()
//                .adminId("newAdminId")
//                .adminName("newAdmin")
//                .adminPwd("newPassword")
//                .adminEmail("newAdmin@example.com")
//                .adminPhone("010-5678-1234")
//                .franchiseList(new ArrayList<>())
//                .build();
//
//        // When
//        ResponseEntity<String> response = adminInfoService.registerAdmin(newAdminDTO, requestorAdmin.getAdminCode());
//
//        // Then
//        assertEquals(403, response.getStatusCodeValue());
//        assertEquals("신규 관리자 등록은 루트 관리자만 가능합니다.", response.getBody());
//    }
//
//    @Test
//    @Transactional
//    public void 관리자_정보_수정() {
//        // Given
//        AdminDTO updatedAdminDTO = AdminDTO.builder()
//                .adminName("updatedAdmin")
//                .adminPwd("updatedPassword")
//                .adminEmail("updated@example.com")
//                .adminPhone("010-9876-5432")
//                .build();
//
//        // When
//        ResponseEntity<String> response = adminInfoService.updateAdminInfo(admin.getAdminCode(), updatedAdminDTO);
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("관리자 정보 수정이 완료되었습니다.", response.getBody());
//
//        // 수정된 정보 확인
//        AdminDTO updatedAdmin = adminInfoService.findAdminById(admin.getAdminCode()).getBody();
//        assertEquals("updatedAdmin", updatedAdmin.getAdminName());
//        assertEquals("updated@example.com", updatedAdmin.getAdminEmail());
//        assertEquals("010-9876-5432", updatedAdmin.getAdminPhone());
//    }
//
//    @Test
//    @Transactional
//    public void 관리자_삭제() {
//        // Given
//        AdminDTO newAdminDTO = AdminDTO.builder()
//                .adminId("newAdminId")
//                .adminName("newAdmin")
//                .adminPwd("newPassword")
//                .adminEmail("newAdmin@example.com")
//                .adminPhone("010-5678-1234")
//                .franchiseList(new ArrayList<>())
//                .build();
//        adminInfoService.registerAdmin(newAdminDTO, admin.getAdminCode());
//        AdminDTO newAdmin = adminInfoService.findAdminList().stream()
//                .filter(a -> a.getAdminId().equals("newAdminId"))
//                .findFirst()
//                .orElse(null);
//        assertNotNull(newAdmin);
//
//        // When
//        ResponseEntity<String> response = adminInfoService.deleteAdmin(newAdmin.getAdminCode(), admin.getAdminCode());
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("관리자 비활성화(삭제)가 완료됨.", response.getBody());
//
//        // 비활성화된 관리자 확인
//        AdminDTO deletedAdmin = adminInfoService.findAdminById(newAdmin.getAdminCode()).getBody();
//        assertNotNull(deletedAdmin);
//        assertFalse(deletedAdmin.isAdminStatus());
//    }
//}
