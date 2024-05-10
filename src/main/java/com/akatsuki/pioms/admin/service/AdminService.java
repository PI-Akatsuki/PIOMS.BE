package com.akatsuki.pioms.admin.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    // 전체 목록 조회
    List<Admin> findAdminList();

    // 상세 조회
    Optional<Admin> findAdminById(int adminCode);

    // 관리자 등록
    ResponseEntity<String> saveAdmin(Admin admin, int requestorAdminCode);

    // 관리자 수정
    ResponseEntity<String> updateAdminInfo(int adminCode, Admin updatedAdmin, int requestorAdminCode);

    // 비활성화(삭제)
    ResponseEntity<String> deleteAdmin(int adminCode);

}