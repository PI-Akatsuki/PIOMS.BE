package com.akatsuki.pioms.login.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.dto.AdminDTO;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.dto.DeliveryDriverDTO;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
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
    public ResponseEntity<AdminDTO> adminLogin(String adminId, String adminPassword, String accessNumber) {
        Optional<Admin> optionalAdmin = adminRepository.findByAdminId(adminId);

        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            if (admin.getPwdCheckCount() >= 5) {
                logger.warning("로그인 시도 횟수 초과 (관리자)");
                return ResponseEntity.status(403).body(null);
            }
            if (passwordEncoder.matches(adminPassword, admin.getAdminPwd()) && accessNumber.equals(admin.getAccessNumber())) {
                admin.setPwdCheckCount(0); // 로그인 성공 시 시도 횟수 초기화
                adminRepository.save(admin);
                return authenticateAndGenerateTokens(new AdminDTO(admin), admin.getAdminId(), adminPassword, admin.getAdminCode(), admin.getAdminName(), admin.getAdminRole());
            } else {
                admin.setPwdCheckCount(admin.getPwdCheckCount() + 1); // 로그인 시도 횟수 증가
                adminRepository.save(admin);
                logger.warning("비밀번호 또는 접근 번호가 일치하지 않음 (관리자)");
            }
        } else {
            logger.warning("관리자를 찾을 수 없음: " + adminId);
        }

        return ResponseEntity.status(401).build();
    }

    @Override
    public ResponseEntity<FranchiseOwnerDTO> franchiseOwnerLogin(String franchiseOwnerId, String franchiseOwnerPassword) {
        Optional<FranchiseOwner> optionalFranchiseOwner = franchiseOwnerRepository.findByFranchiseOwnerId(franchiseOwnerId);

        if (optionalFranchiseOwner.isPresent()) {
            FranchiseOwner franchiseOwner = optionalFranchiseOwner.get();
            if (franchiseOwner.getOwnerPwdCheckCount() >= 5) {
                logger.warning("로그인 시도 횟수 초과 (프랜차이즈 점주)");
                return ResponseEntity.status(403).body(null);
            }
            if (passwordEncoder.matches(franchiseOwnerPassword, franchiseOwner.getFranchiseOwnerPwd())) {
                franchiseOwner.setOwnerPwdCheckCount(0); // 로그인 성공 시 시도 횟수 초기화
                franchiseOwnerRepository.save(franchiseOwner);
                return authenticateAndGenerateTokens(new FranchiseOwnerDTO(franchiseOwner), franchiseOwner.getFranchiseOwnerId(), franchiseOwnerPassword, franchiseOwner.getFranchiseOwnerCode(), franchiseOwner.getFranchiseOwnerName(), franchiseOwner.getFranchiseRole());
            } else {
                franchiseOwner.setOwnerPwdCheckCount(franchiseOwner.getOwnerPwdCheckCount() + 1); // 로그인 시도 횟수 증가
                franchiseOwnerRepository.save(franchiseOwner);
                logger.warning("비밀번호가 일치하지 않음 (프랜차이즈 점주)");
            }
        } else {
            logger.warning("프랜차이즈 점주를 찾을 수 없음: " + franchiseOwnerId);
        }

        return ResponseEntity.status(401).build();
    }

    @Override
    public ResponseEntity<DeliveryDriverDTO> deliveryDriverLogin(String driverId, String driverPassword) {
        Optional<DeliveryDriver> optionalDeliveryDriver = deliveryDriverRepository.findByDriverId(driverId);

        if (optionalDeliveryDriver.isPresent()) {
            DeliveryDriver deliveryDriver = optionalDeliveryDriver.get();
            if (deliveryDriver.getDriverPwdCheckCount() >= 5) {
                logger.warning("로그인 시도 횟수 초과 (배송기사)");
                return ResponseEntity.status(403).body(null);
            }
            if (passwordEncoder.matches(driverPassword, deliveryDriver.getDriverPwd())) {
                deliveryDriver.setDriverPwdCheckCount(0); // 로그인 성공 시 시도 횟수 초기화
                deliveryDriverRepository.save(deliveryDriver);
                return authenticateAndGenerateTokens(new DeliveryDriverDTO(deliveryDriver), deliveryDriver.getDriverId(), driverPassword, deliveryDriver.getDriverCode(), deliveryDriver.getDriverName(), deliveryDriver.getDriverRole());
            } else {
                deliveryDriver.setDriverPwdCheckCount(deliveryDriver.getDriverPwdCheckCount() + 1); // 로그인 시도 횟수 증가
                deliveryDriverRepository.save(deliveryDriver);
                logger.warning("비밀번호가 일치하지 않음 (배송기사)");
            }
        } else {
            logger.warning("배송기사를 찾을 수 없음: " + driverId);
        }

        return ResponseEntity.status(401).build();
    }

    private <T> ResponseEntity<T> authenticateAndGenerateTokens(T userDTO, String userId, String password, int userCode, String userName, String role) {
        try {
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(userId, password));
            CustomUserDetails authenticatedUserDetails = (CustomUserDetails) authentication.getPrincipal();

            String accessToken = createAccessToken(userCode, userId, userName, role);
            String refreshToken = createRefreshToken(userCode, userId, userName, role);

            redisTokenService.saveRefreshToken(userId, refreshToken);

            logger.info("생성된 Access Token: " + accessToken);
            logger.info("생성된 Refresh Token: " + refreshToken);

            if (userDTO instanceof AdminDTO) {
                logService.saveLog(userName, LogStatus.로그인, "관리자 로그인", "Login");
            } else if (userDTO instanceof FranchiseOwnerDTO) {
                logService.saveLog(userName, LogStatus.로그인, "가맹점주 로그인", "Login");
            } else if (userDTO instanceof DeliveryDriverDTO) {
                logService.saveLog(userName, LogStatus.로그인, "배송기사 로그인", "Login");
            }

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Set-Cookie", "refresh=" + refreshToken + "; HttpOnly; Path=/; Max-Age=86400000;")
                    .body(userDTO);
        } catch (AuthenticationException e) {
            logger.warning("인증 실패: " + e.getMessage());
            return ResponseEntity.status(403).build();
        }
    }

    private String createAccessToken(int usercode, String userId, String username, String role) {
        return jwtUtil.createJwt("access", usercode, userId, username, role, 86400000L);
    }

    private String createRefreshToken(int usercode, String userId, String username, String role) {
        return jwtUtil.createJwt("refresh", usercode, userId, username, role, 864000000L);
    }
}
