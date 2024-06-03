package com.akatsuki.pioms.dashboard.service;

import com.akatsuki.pioms.dashboard.aggregate.ResponseAdminDashBoard;
import com.akatsuki.pioms.dashboard.aggregate.ResponseDriverDashBoard;

public interface DashboardService {
    ResponseAdminDashBoard getRootDash();

    ResponseAdminDashBoard getAdminDash();

    ResponseDriverDashBoard getDriverDashBoard();

}
