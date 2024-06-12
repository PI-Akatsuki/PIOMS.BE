package com.akatsuki.pioms.dashboard.service;

import com.akatsuki.pioms.dashboard.aggregate.ResponseAdminDashBoard;
import com.akatsuki.pioms.dashboard.aggregate.ResponseDriverDashBoard;
import com.akatsuki.pioms.dashboard.aggregate.ResponseFranchiseDashBoard;

public interface DashboardService {
//    ResponseAdminDashBoard getRootDash();

    ResponseAdminDashBoard getAdminDash();

    ResponseFranchiseDashBoard getFranchiseDash();

    ResponseDriverDashBoard getDriverDashBoard();

}
