package com.akatsuki.pioms.login.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.user.aggregate.User;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    // 관리자 로그인
    ResponseEntity<User> adminLogin(String adminId, String password, String accessNumber);

    // 오너 로그인
    ResponseEntity<User> driverLogin(String driverId, String driverPassword);

    // 배송기사 로그인
    ResponseEntity<User> frOwnerLogin(String frOwnerId, String frOwnerPassword);
}
