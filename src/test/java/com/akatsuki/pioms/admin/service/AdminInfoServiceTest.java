package com.akatsuki.pioms.admin.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.config.MockRedisConfig;
import com.akatsuki.pioms.log.service.LogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Import(MockRedisConfig.class)
@Transactional
class AdminInfoServiceTest {

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private LogServiceImpl logService;

    @Autowired
    private AdminInfoServiceImpl adminInfoService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = Admin.builder()
                .adminCode(1)
                .adminId("root")
                .adminPwd(passwordEncoder.encode("root")) // 암호화된 비밀번호 설정
                .accessNumber("rootAccess")
                .adminStatus(true)
                .adminName("root")
                .adminEmail("root@example.com")
                .adminPhone("010-1234-5678")
                .enrollDate("2023-01-01 00:00:00")
                .updateDate("2023-01-01 00:00:00")
                .adminRole("ROLE_ROOT")
                .franchise(new ArrayList<>())
                .build();

        when(adminRepository.findById(1)).thenReturn(Optional.of(admin));
    }

    @Test
    @DisplayName("관리자 전체조회 - 성공")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void findAllAdmins() {
        System.err.println("관리자 전체조회 - 테스트 시작");

        // Given
        List<Admin> adminList = new ArrayList<>();
        adminList.add(admin);
        when(adminRepository.findAllByOrderByEnrollDateDesc()).thenReturn(adminList);

        // When
        List<AdminDTO> result = adminInfoService.findAdminList();

        // Then
        assertNotNull(result);
        assertTrue(result.size() > 0);

        System.err.println("관리자 전체조회 - 테스트 성공");
    }

    @Test
    @DisplayName("관리자 상세조회 - 성공")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void findAdminById() {
        System.err.println("관리자 상세조회 - 테스트 시작");

        // When
        ResponseEntity<AdminDTO> response = adminInfoService.findAdminById(admin.getAdminCode());
        AdminDTO adminDTO = response.getBody();

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(adminDTO);
        assertEquals(admin.getAdminId(), adminDTO.getAdminId());

        System.err.println("관리자 상세조회 - 테스트 성공");
    }

    @Test
    @DisplayName("관리자 등록 - 성공")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void registerAdmin() {
        System.err.println("관리자 등록 - 테스트 시작");

        // Given
        AdminDTO newAdminDTO = AdminDTO.builder()
                .adminName("newAdmin")
                .adminId("newAdminId")
                .adminPwd("newPassword")
                .adminEmail("newAdmin@example.com")
                .adminPhone("010-5678-1234")
                .accessNumber("newAccessNumber")
                .adminRole("ROLE_ADMIN")
                .adminStatus(true)
                .build();

        when(adminRepository.findByAdminId(newAdminDTO.getAdminId())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(newAdminDTO.getAdminPwd())).thenReturn("encodedPassword");

        // When
        ResponseEntity<String> response = adminInfoService.registerAdmin(newAdminDTO);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("신규 관리자 등록이 완료되었습니다.", response.getBody());
        verify(adminRepository, times(1)).save(any(Admin.class));

        System.err.println("관리자 등록 - 테스트 성공");
    }

    @Test
    @DisplayName("관리자 등록 - 권한 없음")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registerAdminNoPermission() {
        System.err.println("관리자 등록 - 권한 없음 테스트 시작");

        // Given
        AdminDTO newAdminDTO = AdminDTO.builder()
                .adminName("newAdmin")
                .adminId("newAdminId")
                .adminPwd("newPassword")
                .adminEmail("newAdmin@example.com")
                .adminPhone("010-5678-1234")
                .accessNumber("newAccessNumber")
                .adminRole("ROLE_ADMIN")
                .adminStatus(true)
                .build();

        // When
        ResponseEntity<String> response = adminInfoService.registerAdmin(newAdminDTO);

        // Then
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("신규 관리자 등록은 루트 관리자만 가능합니다.", response.getBody());

        System.err.println("관리자 등록 - 권한 없음 테스트 성공");
    }

    @Test
    @DisplayName("관리자 정보 수정 - 성공")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void updateAdminInfo() {
        System.err.println("관리자 정보 수정 - 테스트 시작");

        // Given
        AdminDTO updatedAdminDTO = AdminDTO.builder()
                .adminName("updatedAdmin")
                .adminPwd("updatedPassword")
                .adminEmail("updated@example.com")
                .adminPhone("010-1234-5678")
                .build();

        when(passwordEncoder.encode(updatedAdminDTO.getAdminPwd())).thenReturn("newEncodedPassword");
        when(passwordEncoder.matches(updatedAdminDTO.getAdminPwd(), admin.getAdminPwd())).thenReturn(false);

        // When
        ResponseEntity<String> response = adminInfoService.updateAdminInfo(1, updatedAdminDTO);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("관리자 정보 수정이 완료되었습니다.", response.getBody());
        verify(adminRepository, times(1)).save(any(Admin.class));

        System.err.println("관리자 정보 수정 - 테스트 성공");
    }

    @Test
    @DisplayName("관리자 삭제 - 성공")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void deleteAdmin() {
        System.err.println("관리자 삭제 - 테스트 시작");

        // Given
        Admin adminToDelete = Admin.builder()
                .adminId("newAdminId")
                .adminName("newAdmin")
                .adminPwd("newPassword")
                .adminEmail("newAdmin@example.com")
                .adminPhone("010-5678-1234")
                .adminRole("ROLE_ADMIN")
                .adminStatus(true)
                .accessNumber("123abc")
                .build();
        adminToDelete.setAdminCode(2);

        when(adminRepository.findById(2)).thenReturn(Optional.of(adminToDelete));

        // When
        ResponseEntity<String> response = adminInfoService.deleteAdmin(2);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("관리자 비활성화(삭제)가 완료되었습니다.", response.getBody());
        verify(adminRepository, times(1)).save(adminToDelete);
        assertFalse(adminToDelete.isAdminStatus());

        System.err.println("관리자 삭제 - 테스트 성공");
    }

    @Test
    @DisplayName("관리자 비밀번호 초기화 - 성공")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void resetAdminPassword() {
        System.err.println("관리자 비밀번호 초기화 - 테스트 시작");

        // Given
        Admin adminToReset = Admin.builder()
                .adminId("adminToReset")
                .adminPwd("oldPassword")
                .adminCode(3)
                .build();

        when(adminRepository.findById(3)).thenReturn(Optional.of(adminToReset));
        when(passwordEncoder.encode("1234")).thenReturn("encodedNewPassword");

        // When
        ResponseEntity<String> response = adminInfoService.resetAdminPassword(3);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("관리자 비밀번호 초기화가 완료되었습니다.", response.getBody());
        verify(adminRepository, times(1)).save(adminToReset);
        assertEquals("encodedNewPassword", adminToReset.getAdminPwd());

        System.err.println("관리자 비밀번호 초기화 - 테스트 성공");
    }
}
