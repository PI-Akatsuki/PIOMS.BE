package com.akatsuki.pioms.dashboard.controller;

import com.akatsuki.pioms.dashboard.aggregate.ResponseAdminDashBoard;
import com.akatsuki.pioms.dashboard.aggregate.ResponseDriverDashBoard;
import com.akatsuki.pioms.dashboard.aggregate.ResponseFranchiseDashBoard;
import com.akatsuki.pioms.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "[공통]대시보드 API")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }


    @GetMapping("/admin/adminDashboard")
    @Operation(summary = "관리자 대시보드 조회")
    public ResponseEntity<ResponseAdminDashBoard> getAdminDash(){
        ResponseAdminDashBoard responseAdminDashBoard = dashboardService.getAdminDash();
        return ResponseEntity.ok(responseAdminDashBoard);
    }

    @GetMapping("/franchise/franchiseDashboard")
    @Operation(summary = "점주 대시보드 조회")
    public ResponseEntity<ResponseFranchiseDashBoard> getFranchiseDash(){
        ResponseFranchiseDashBoard responseFranchiseDashBoard = dashboardService.getFranchiseDash();
        return ResponseEntity.ok(responseFranchiseDashBoard);
    }

    @GetMapping("/driver/dashboard")
    @Operation(summary = "배송기사 대시보드 조회")
    public ResponseEntity<ResponseDriverDashBoard> getDriverDashBoard() {
        ResponseDriverDashBoard driverDashBoard = dashboardService.getDriverDashBoard();
        return ResponseEntity.ok(driverDashBoard);
    }
}
