package com.akatsuki.pioms.login.controller;

import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.driver.dto.DeliveryDriverDTO;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.login.aggregate.AdminLoginRequest;
import com.akatsuki.pioms.login.aggregate.DeliveryDriverLoginRequest;
import com.akatsuki.pioms.login.aggregate.FranchiseOwnerLoginRequest;
import com.akatsuki.pioms.login.service.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@Tag(name = "[공통]로그인 API")
public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/admin/login")
    public ResponseEntity<AdminDTO> adminLogin(@RequestBody AdminLoginRequest adminLoginRequest) {
        if (adminLoginRequest == null) {
            logger.warning("AdminLoginRequest가 null입니다");
            return ResponseEntity.badRequest().build();
        }
        logger.info("AdminLoginRequest 수신됨: " + adminLoginRequest);
        return loginService.adminLogin(adminLoginRequest.getAdminId(), adminLoginRequest.getPassword(), adminLoginRequest.getAccessNumber());
    }

    @PostMapping("/franchise/login")
    public ResponseEntity<FranchiseOwnerDTO> franchiseOwnerLogin(@RequestBody FranchiseOwnerLoginRequest franchiseOwnerLoginRequest) {
        if (franchiseOwnerLoginRequest == null) {
            logger.warning("FranchiseOwnerLoginRequest가 null입니다");
            return ResponseEntity.badRequest().build();
        }
        logger.info("FranchiseOwnerLoginRequest 수신됨: " + franchiseOwnerLoginRequest);
        return loginService.franchiseOwnerLogin(franchiseOwnerLoginRequest.getFrOwnerId(), franchiseOwnerLoginRequest.getFrOwnerPassword());
    }

    @PostMapping("/driver/login")
    public ResponseEntity<DeliveryDriverDTO> driverLogin(@RequestBody DeliveryDriverLoginRequest deliveryDriverLoginRequest) {
        if (deliveryDriverLoginRequest == null) {
            logger.warning("DeliveryDriverLoginRequest가 null입니다");
            return ResponseEntity.badRequest().build();
        }
        logger.info("DeliveryDriverLoginRequest 수신됨: " + deliveryDriverLoginRequest);
        return loginService.deliveryDriverLogin(deliveryDriverLoginRequest.getDriverId(), deliveryDriverLoginRequest.getDriverPassword());
    }
}
