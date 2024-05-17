package com.akatsuki.pioms.admin.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.login.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AdminInfoServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AdminInfoService adminInfoService;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Admin admin;

    @BeforeEach
    public void setup() {
        admin = new Admin();
        admin.setAdminCode(1);
        admin.setAdminId("root");
        admin.setAdminPwd("root");  // 평문 비밀번호 설정
        admin.setAccessNumber("rootAccess");
        admin.setAdminStatus(true);
        admin.setAdminName("root");  // NullPointerException 방지
        admin.setAdminEmail("root@example.com");  // NullPointerException 방지
        admin.setAdminPhone("010-1234-5678");  // NullPointerException 방지
        admin.setEnrollDate("2023-01-01 00:00:00");
        admin.setUpdateDate("2023-01-01 00:00:00");
        admin.setFranchise(new ArrayList<>());  // 프랜차이즈 리스트 초기화
    }

    @Test
    public void testLoginSuccess() {
        // Given
        when(adminRepository.findByAdminId(anyString())).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);  // 패스워드 매칭 성공

        // When
        ResponseEntity<Admin> response = loginService.adminLogin("root", "root", "rootAccess");

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    public void testLoginFailure() {
        // Given
        when(adminRepository.findByAdminId(anyString())).thenReturn(Optional.empty());

        // When
        ResponseEntity<Admin> response = loginService.adminLogin("root", "wrongpassword", "rootAccess");

        // Then
        assertEquals(401, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    public void testSaveAdmin() {
        // Given
        when(adminRepository.findById(anyInt())).thenReturn(Optional.of(admin));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        Admin newAdmin = new Admin();
        newAdmin.setAdminName("newAdmin");
        newAdmin.setAdminPwd("newPassword");
        newAdmin.setFranchise(new ArrayList<>());

        // When
        ResponseEntity<String> response = adminInfoService.saveAdmin(newAdmin, 1);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("신규 관리자 등록이 완료되었습니다.", response.getBody());
    }

    @Test
    public void testSaveAdminUnauthorized() {
        // Given
        Admin requestorAdmin = new Admin();
        requestorAdmin.setAdminCode(2); // 루트 관리자가 아닐 경우

        when(adminRepository.findById(anyInt())).thenReturn(Optional.of(requestorAdmin));

        Admin newAdmin = new Admin();
        newAdmin.setAdminName("newAdmin");
        newAdmin.setAdminPwd("newPassword");
        newAdmin.setFranchise(new ArrayList<>());

        // When
        ResponseEntity<String> response = adminInfoService.saveAdmin(newAdmin, 2);

        // Then
        assertEquals(403, response.getStatusCodeValue());
        assertEquals("신규 관리자 등록은 루트 관리자만 가능합니다.", response.getBody());
    }

    @Test
    public void testUpdateAdminInfo() {
        // Given
        when(adminRepository.findById(anyInt())).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);  // 비밀번호가 다르다고 가정
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        Admin updatedAdmin = new Admin();
        updatedAdmin.setAdminName("updatedAdmin");
        updatedAdmin.setAdminPwd("updatedPassword");
        updatedAdmin.setAdminEmail("updated@example.com");
        updatedAdmin.setAdminPhone("010-1234-5678");

        // When
        ResponseEntity<String> response = adminInfoService.updateAdminInfo(1, updatedAdmin);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("관리자 정보 수정이 완료되었습니다.", response.getBody());
    }

    @Test
    public void testDeleteAdmin() {
        // Given
        when(adminRepository.findById(anyInt())).thenReturn(Optional.of(admin));

        // When
        ResponseEntity<String> response = adminInfoService.deleteAdmin(2, 1);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("관리자 비활성화(삭제)가 완료됨.", response.getBody());
    }
}
