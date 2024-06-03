package com.akatsuki.pioms.dashboard.controller;

import com.akatsuki.pioms.dashboard.aggregate.ResponseAdminDashBoard;
import com.akatsuki.pioms.dashboard.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        ResponseAdminDashBoard responseAdminDashBoard = dashboardService.getRootDash();
        return ResponseEntity.ok(responseAdminDashBoard);
    }
    @GetMapping("/admin/adminDashboard")
    public ResponseEntity<ResponseAdminDashBoard> getAdminDash(){
        ResponseAdminDashBoard responseAdminDashBoard = dashboardService.getAdminDash();
        return ResponseEntity.ok(responseAdminDashBoard);
    }
}
