package com.akatsuki.pioms.login.controller;

import com.akatsuki.pioms.login.aggregate.AdminLoginRequest;
import com.akatsuki.pioms.login.aggregate.DeliveryDriverLoginRequest;
import com.akatsuki.pioms.login.aggregate.FranchiseOwnerLoginRequest;
import com.akatsuki.pioms.login.service.LoginService;
import com.akatsuki.pioms.user.aggregate.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("admin/login")
    public ResponseEntity<User> adminLogin(@RequestBody AdminLoginRequest adminLoginRequest) {
        return loginService.adminLogin(adminLoginRequest.getAdminId(), adminLoginRequest.getPassword(), adminLoginRequest.getAccessNumber());
    }

    @PostMapping("franchise/login")
    public ResponseEntity<User> frOwnerLogin(@RequestBody FranchiseOwnerLoginRequest franchiseOwnerLoginRequest) {
        return loginService.frOwnerLogin(franchiseOwnerLoginRequest.getFrOwnerId(), franchiseOwnerLoginRequest.getFrOwnerPassword());
    }

    @PostMapping("driver/login")
    public ResponseEntity<User> driverLogin(@RequestBody DeliveryDriverLoginRequest deliveryDriverLoginRequest) {
        return loginService.driverLogin(deliveryDriverLoginRequest.getDriverId(), deliveryDriverLoginRequest.getDriverPassword());
    }
}
