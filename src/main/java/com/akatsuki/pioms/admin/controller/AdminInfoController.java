package com.akatsuki.pioms.admin.controller;

import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.admin.service.AdminInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
@Tag(name = "본사 관리자", description = "Company admin")
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
    @Operation(summary = "본사 관리자 상세조회", description = "본사 관리자를 조회합니다.")
    @GetMapping("/list/detail/{adminCode}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable int adminCode) {
        return adminService.findAdminById(adminCode);
    }

    // 관리자 등록
    @Operation(summary = "본사 관리자 등록", description = "본사 관리자를 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<String> registerAdmin(
            // Root관리자만 등록이 가능
            @RequestBody AdminDTO adminDTO,
            @RequestParam int requestorAdminCode
    ) {
        return adminService.registerAdmin(adminDTO, requestorAdminCode);
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
    public ResponseEntity<String> deleteAdmin(
            @PathVariable int adminCode,
            @RequestParam int requestorAdminCode
    ) {
        return adminService.deleteAdmin(adminCode, requestorAdminCode);
    }
}
