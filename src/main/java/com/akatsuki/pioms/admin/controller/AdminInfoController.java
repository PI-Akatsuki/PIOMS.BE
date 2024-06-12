package com.akatsuki.pioms.admin.controller;

import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.admin.service.AdminInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Tag(name = "[관리자]관리자 정보 API", description = "Admin Info")
public class AdminInfoController {

    private final AdminInfoService adminService;

    @Autowired
    public AdminInfoController(AdminInfoService adminService) {
        this.adminService = adminService;
    }

    // 관리자 전체 조회
    @Operation(summary = "본사 관리자 전체 조회", description = "본사 관리자 전체 조회")
    @GetMapping("/list")
    public ResponseEntity<List<AdminDTO>> getAdminList() {
        List<AdminDTO> adminList = adminService.findAdminList();
        return ResponseEntity.ok(adminList);
    }

    // 관리자 상세 조회
    @Operation(summary = "본사 관리자 상세 조회", description = "본사 관리자를 조회합니다.")
    @GetMapping("/list/detail/{adminCode}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable int adminCode) {
        return adminService.findAdminById(adminCode);
    }

    // 관리자 등록
    @Operation(summary = "본사 관리자 등록", description = "본사 관리자를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminDTO adminDTO) {
        return adminService.registerAdmin(adminDTO);
    }

    // 관리자 정보 수정
    @Operation(summary = "본사 관리자 정보 수정", description = "본사 관리자 정보를 수정합니다.")
    @PutMapping("/update/{adminCode}")
    public ResponseEntity<String> updateAdmin(
            @PathVariable int adminCode,
            @RequestBody AdminDTO updatedAdminDTO
    ) {
        return adminService.updateAdminInfo(adminCode, updatedAdminDTO);
    }

    // 관리자 비활성화(삭제)
    @Operation(summary = "본사 관리자 삭제", description = "본사 관리자를 삭제합니다.")
    @DeleteMapping("/delete/{adminCode}")
    public ResponseEntity<String> deleteAdmin(@PathVariable int adminCode) {
        return adminService.deleteAdmin(adminCode);
    }

    // 관리자 비밀번호 초기화
    @Operation(summary = "본사 관리자 비밀번호 초기화", description = "본사 관리자 비밀번호를 초기화합니다.")
    @PostMapping("/reset-password/{adminCode}")
    public ResponseEntity<String> resetAdminPassword(@PathVariable int adminCode) {
        return adminService.resetAdminPassword(adminCode);
    }

    // 가맹점 추가
    @Operation(summary = "가맹점 추가", description = "관리자에게 가맹점을 추가합니다.")
    @PostMapping("/add-franchise/{adminCode}")
    public ResponseEntity<String> addFranchise(@PathVariable int adminCode, @RequestBody Map<String, Integer> request) {
        return adminService.addFranchise(adminCode, request.get("franchiseCode"));
    }

    // 가맹점 제거
    @Operation(summary = "가맹점 제거", description = "관리자에게서 가맹점을 제거합니다.")
    @DeleteMapping("/remove-franchise/{adminCode}")
    public ResponseEntity<String> removeFranchise(@PathVariable int adminCode, @RequestBody Map<String, Integer> request) {
        return adminService.removeFranchise(adminCode, request.get("franchiseCode"));
    }
}
