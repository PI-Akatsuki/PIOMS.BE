package com.akatsuki.pioms.admin.service;

import com.akatsuki.pioms.admin.dto.AdminDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminInfoService {

    // 전체 목록 조회
    List<AdminDTO> findAdminList();

    // 상세 조회
    ResponseEntity<AdminDTO> findAdminById(int adminCode);

    // 관리자 등록
    ResponseEntity<String> registerAdmin(AdminDTO adminDTO);

    // 관리자 수정
    ResponseEntity<String> updateAdminInfo(int adminCode, AdminDTO updatedAdminDTO);

    // 비활성화(삭제)
    ResponseEntity<String> deleteAdmin(int adminCode);

    // 비밀번호 초기화
    ResponseEntity<String> resetAdminPassword(int adminCode);

    // 가맹점 추가
    ResponseEntity<String> addFranchise(int adminCode, int franchiseCode);

    // 가맹점 제거
    ResponseEntity<String> removeFranchise(int adminCode, int franchiseCode);
}
