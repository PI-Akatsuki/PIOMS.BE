package com.akatsuki.pioms.login.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<Admin> adminLogin(String adminId, String adminPassword, String accessNumber);
    ResponseEntity<FranchiseOwner> franchiseOwnerLogin(String franchiseOwnerId, String franchiseOwnerPassword);
    ResponseEntity<DeliveryDriver> deliveryDriverLogin(String driverId, String driverPassword);
}
