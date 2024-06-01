package com.akatsuki.pioms.login.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.jwt.JWTUtil;
import com.akatsuki.pioms.user.dto.CustomUserDetails;
import com.akatsuki.pioms.log.service.LogService;
import com.akatsuki.pioms.log.etc.LogStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class LoginServiceImpl implements LoginService {

    private final AdminRepository adminRepository;
    private final FranchiseOwnerRepository franchiseOwnerRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final RedisTokenService redisTokenService;
    private final LogService logService;

    @Autowired
    public LoginServiceImpl(AdminRepository adminRepository,
                            FranchiseOwnerRepository franchiseOwnerRepository,
                            DeliveryDriverRepository deliveryDriverRepository,
                            JWTUtil jwtUtil,
                            PasswordEncoder passwordEncoder,
                            AuthenticationProvider authenticationProvider,
                            RedisTokenService redisTokenService,
                            LogService logService) {
        this.adminRepository = adminRepository;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.redisTokenService = redisTokenService;
        this.logService = logService;
    }

    private static final Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());

    @Override
    public ResponseEntity<Admin> adminLogin(String adminId, String adminPassword, String accessNumber) {
        Optional<Admin> optionalAdmin = adminRepository.findByAdminId(adminId);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            if (passwordEncoder.matches(adminPassword, admin.getAdminPwd()) && accessNumber.equals(admin.getAccessNumber())) {
                return authenticateAndGenerateTokens(admin, admin.getAdminId(), adminPassword, admin.getAdminCode(), admin.getAdminName(), admin.getAdminRole());
            } else {
                logger.warning("비밀번호 또는 접근 번호가 일치하지 않음 (관리자)");
            }
        } else {
            logger.warning("관리자를 찾을 수 없음: " + adminId);
        }

        return ResponseEntity.status(401).build();
    }

    @Override
    public ResponseEntity<FranchiseOwner> franchiseOwnerLogin(String franchiseOwnerId, String franchiseOwnerPassword) {
        Optional<FranchiseOwner> optionalFranchiseOwner = franchiseOwnerRepository.findByFranchiseOwnerId(franchiseOwnerId);

        if (optionalFranchiseOwner.isPresent()) {
            FranchiseOwner franchiseOwner = optionalFranchiseOwner.get();
            if (passwordEncoder.matches(franchiseOwnerPassword, franchiseOwner.getFranchiseOwnerPwd())) {
                return authenticateAndGenerateTokens(franchiseOwner, franchiseOwner.getFranchiseOwnerId(), franchiseOwnerPassword, franchiseOwner.getFranchiseOwnerCode(), franchiseOwner.getFranchiseOwnerName(), franchiseOwner.getFranchiseRole());
            } else {
                logger.warning("비밀번호가 일치하지 않음 (프랜차이즈 소유자)");
            }
        } else {
            logger.warning("프랜차이즈 소유자를 찾을 수 없음: " + franchiseOwnerId);
        }

        return ResponseEntity.status(401).build();
    }

    @Override
    public ResponseEntity<DeliveryDriver> deliveryDriverLogin(String driverId, String driverPassword) {
        Optional<DeliveryDriver> optionalDeliveryDriver = deliveryDriverRepository.findByDriverId(driverId);

        if (optionalDeliveryDriver.isPresent()) {
            DeliveryDriver deliveryDriver = optionalDeliveryDriver.get();
            if (passwordEncoder.matches(driverPassword, deliveryDriver.getDriverPwd())) {
                return authenticateAndGenerateTokens(deliveryDriver, deliveryDriver.getDriverId(), driverPassword, deliveryDriver.getDriverCode(), deliveryDriver.getDriverName(), deliveryDriver.getDriverRole());
            } else {
                logger.warning("비밀번호가 일치하지 않음 (배송기사)");
            }
        } else {
            logger.warning("배송기사를 찾을 수 없음: " + driverId);
        }

        return ResponseEntity.status(401).build();
    }

    private <T> ResponseEntity<T> authenticateAndGenerateTokens(T user, String userId, String password, int userCode, String userName, String role) {
        try {
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, password));
            CustomUserDetails authenticatedUserDetails = (CustomUserDetails) authentication.getPrincipal();

            String accessToken = createAccessToken(userCode, userId, userName, role);
            String refreshToken = createRefreshToken(userCode, userId, userName, role);

            redisTokenService.saveRefreshToken(userId, refreshToken);

            logger.info("생성된 Access Token: " + accessToken);
            logger.info("생성된 Refresh Token: " + refreshToken);

            if (user instanceof Admin) {
                logService.saveLog(userName, LogStatus.등록, "관리자 로그인", "Login");
            } else if (user instanceof FranchiseOwner) {
                logService.saveLog(userName, LogStatus.등록, "가맹점주 로그인", "Login");
            } else if (user instanceof DeliveryDriver) {
                logService.saveLog(userName, LogStatus.등록, "배송기사 로그인", "Login");
            }

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Set-Cookie", "refresh=" + refreshToken + "; HttpOnly; Path=/; Max-Age=86400000;")
                    .body(user);
        } catch (AuthenticationException e) {
            logger.warning("인증 실패: " + e.getMessage());
            return ResponseEntity.status(403).build();
        }
    }

    private String createAccessToken(int usercode, String userId, String username, String role) {
        return jwtUtil.createJwt("access", usercode, userId, username, role, 3600000L);
    }

    private String createRefreshToken(int usercode, String userId, String username, String role) {
        return jwtUtil.createJwt("refresh", usercode, userId, username, role, 86400000L);
    }
}
