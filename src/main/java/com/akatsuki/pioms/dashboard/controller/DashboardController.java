package com.akatsuki.pioms.dashboard.controller;

import com.akatsuki.pioms.dashboard.aggregate.ResponseAdminDashBoard;
import com.akatsuki.pioms.dashboard.aggregate.ResponseDriverDashBoard;
import com.akatsuki.pioms.dashboard.aggregate.ResponseFranchiseDashBoard;
import com.akatsuki.pioms.dashboard.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin/rootDashboard")
    public ResponseEntity<ResponseAdminDashBoard> getRootDash(){
        ResponseAdminDashBoard responseAdminDashBoard = dashboardService.getAdminDash();
        return ResponseEntity.ok(responseAdminDashBoard);
    }
    @GetMapping("/admin/adminDashboard")
    public ResponseEntity<ResponseAdminDashBoard> getAdminDash(){
        ResponseAdminDashBoard responseAdminDashBoard = dashboardService.getAdminDash();
        return ResponseEntity.ok(responseAdminDashBoard);
    }

    @GetMapping("/franchise/franchiseDashboard")
    public ResponseEntity<ResponseFranchiseDashBoard> getFranchiseDash(){
        ResponseFranchiseDashBoard responseFranchiseDashBoard = dashboardService.getFranchiseDash();
        return ResponseEntity.ok(responseFranchiseDashBoard);
    }

    @GetMapping("/driver/dashboard")
    public ResponseEntity<ResponseDriverDashBoard> getDriverDashBoard() {
        ResponseDriverDashBoard driverDashBoard = dashboardService.getDriverDashBoard();
        return ResponseEntity.ok(driverDashBoard);
    }
}
