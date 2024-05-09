package com.akatsuki.pioms.admin.controller;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin")
@Tag(name = "본사 관리자", description = "Company admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 관리자 조회
    @Operation(summary = "본사 관리자 전체 조회", description = "본사 관리자 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<List<Admin>> getAdminList() {
        List<Admin> adminList = adminService.findAdminList();
        return ResponseEntity.ok(adminList);
    }

    // 관리자 상세 조회
    @Operation(summary = "본사 관리자 상세조회", description = "본사 관리자를 조회합니다.")
    @GetMapping("/list/detail/{adminCode}")
    public ResponseEntity<Admin> getAdminById(@PathVariable int adminCode) {
        Optional<Admin> admin = adminService.findAdminById(adminCode);
        return admin.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 관리자 등록
    // 관리자 수정
    // 관리자 삭제

}
