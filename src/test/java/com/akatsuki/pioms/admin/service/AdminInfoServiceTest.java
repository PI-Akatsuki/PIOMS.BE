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
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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
//    @Autowired
//    private WebApplicationContext context;
//
//    private MockMvc mockMvc;
//
//    private Admin admin;
//
//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//
//        admin = new Admin();
//        admin.setAdminCode(1);
//        admin.setAdminId("root");
//        admin.setAdminPwd(passwordEncoder.encode("root")); // 암호화된 비밀번호 설정
//        admin.setAccessNumber("rootAccess");
//        admin.setAdminStatus(true);
//        admin.setAdminName("root");
//        admin.setAdminEmail("root@example.com");
//        admin.setAdminPhone("010-1234-5678");
//        admin.setEnrollDate("2023-01-01 00:00:00");
//        admin.setUpdateDate("2023-01-01 00:00:00");
//        admin.setAdminRole("ROLE_ROOT");
//        admin.setFranchise(new ArrayList<>());
//
//        // 데이터베이스에 관리자 저장
//        adminRepository.save(admin);
//    }
//
//
//    @Test
//    @Transactional
//    @WithMockUser(username = "root", roles = {"ROOT"})
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
//    @WithMockUser(username = "root", roles = {"ROOT"})
//    public void 관리자_상세조회() {
//        // When
//        ResponseEntity<AdminDTO> response = adminInfoService.findAdminById(admin.getAdminCode());
//        AdminDTO adminDTO = response.getBody();
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(adminDTO);
//        assertEquals(admin.getAdminId(), adminDTO.getAdminId());
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username = "root", roles = {"ROOT"})
//    public void 관리자_등록() {
//        // Given
//        AdminDTO newAdminDTO = AdminDTO.builder()
//                .adminName("newAdmin")
//                .adminId("newAdminId")
//                .adminPwd("newPassword")
//                .adminEmail("newAdmin@example.com")
//                .adminPhone("010-5678-1234")
//                .accessNumber("newAccessNumber")
//                .adminRole("ROLE_ADMIN")
//                .adminStatus(true)
//                .pwdCheckCount(0)
//                .adminDormancy(false)
//                .build();
//
//        // When
//        ResponseEntity<String> response = adminInfoService.registerAdmin(newAdminDTO);
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("신규 관리자 등록이 완료되었습니다.", response.getBody());
//
//        Admin savedAdmin = adminRepository.findByAdminId("newAdminId").orElseThrow();
//        assertNotNull(savedAdmin);
//        assertEquals("newAdmin", savedAdmin.getAdminName());
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void 관리자_등록_권한없음() {
//        // Given
//        AdminDTO newAdminDTO = AdminDTO.builder()
//                .adminName("newAdmin")
//                .adminId("newAdminId")
//                .adminPwd("newPassword")
//                .adminEmail("newAdmin@example.com")
//                .adminPhone("010-5678-1234")
//                .accessNumber("newAccessNumber")
//                .adminRole("ROLE_ADMIN")
//                .adminStatus(true)
//                .pwdCheckCount(0)
//                .adminDormancy(false)
//                .build();
//
//        // When
//        ResponseEntity<String> response = adminInfoService.registerAdmin(newAdminDTO);
//
//        // Then
//        assertEquals(403, response.getStatusCodeValue());
//        assertEquals("신규 관리자 등록은 루트 관리자만 가능합니다.", response.getBody());
//    }
//
//    @Test
//    @Transactional
//    @WithMockUser(username = "root", roles = {"ROOT"})
//    public void 관리자_정보_수정() {
//        // Given
//        AdminDTO updatedAdminDTO = AdminDTO.builder()
//                .adminName("updatedAdmin")
//                .adminPwd("updatedPassword")
//                .adminEmail("updated@example.com")
//                .adminPhone("010-1234-5678")
//                .build();
//
//        // When
//        ResponseEntity<String> response = adminInfoService.updateAdminInfo(1, updatedAdminDTO);
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("관리자 정보 수정이 완료되었습니다.", response.getBody());
//
//        Admin updatedAdmin = adminRepository.findById(1).orElseThrow();
//        assertEquals("updatedAdmin", updatedAdmin.getAdminName());
//        assertEquals("updated@example.com", updatedAdmin.getAdminEmail());
//    }
//
//
//    @Test
//    @Transactional
//    @WithMockUser(username = "root", roles = {"ROOT"})
//    public void 관리자_삭제() {
//        // Given
//        Admin admin = Admin.builder()
//                .adminId("newAdminId")
//                .adminName("newAdmin")
//                .adminPwd("newPassword")
//                .adminEmail("newAdmin@example.com")
//                .adminPhone("010-5678-1234")
//                .adminRole("ROLE_ADMIN")
//                .adminStatus(true)
//                .accessNumber("123abc")  // 필수 필드 설정
//                .build();
//        adminRepository.save(admin);
//
//        Admin savedAdmin = adminRepository.findByAdminId("newAdminId").orElse(null);
//        assertNotNull(savedAdmin);
//
//        // When
//        ResponseEntity<String> response = adminInfoService.deleteAdmin(savedAdmin.getAdminCode());
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals("관리자 비활성화(삭제)가 완료됨.", response.getBody());
//
//        savedAdmin = adminRepository.findById(savedAdmin.getAdminCode()).orElse(null);
//        assertNotNull(savedAdmin);
//        assertFalse(savedAdmin.isAdminStatus());
//    }
//
//}
