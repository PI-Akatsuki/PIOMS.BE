package com.akatsuki.pioms.login.service;

import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.driver.dto.DeliveryDriverDTO;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<AdminDTO> adminLogin(String adminId, String adminPassword, String accessNumber);
    ResponseEntity<FranchiseOwnerDTO> franchiseOwnerLogin(String franchiseOwnerId, String franchiseOwnerPassword);
    ResponseEntity<DeliveryDriverDTO> deliveryDriverLogin(String driverId, String driverPassword);
}
