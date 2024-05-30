package com.akatsuki.pioms.login.service;

import com.akatsuki.pioms.user.aggregate.User;
import com.akatsuki.pioms.user.dto.CustomUserDetails;
import com.akatsuki.pioms.user.repository.UserRepository;
import com.akatsuki.pioms.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class LoginServiceImpl implements LoginService {

    private static final Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<User> adminLogin(String adminId, String password, String accessNumber) {
        Optional<User> optionalUser = userRepository.findByUserId(adminId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPwdCheckCount() >= 5) {
                logger.warning("비밀번호 틀린 횟수 초과: " + adminId);
                return ResponseEntity.status(403).body(user);
            }

            if (("ROLE_ROOT".equals(user.getRole()) || "ROLE_ADMIN".equals(user.getRole())) &&
                    passwordEncoder.matches(password, user.getPassword()) &&
                    accessNumber.equals(user.getAccessNumber())) {

                // 로그인 성공, 비밀번호 틀린 횟수 초기화
                user.setPwdCheckCount(0);



                userRepository.save(user);

                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(adminId, password));
                CustomUserDetails authenticatedUserDetails = (CustomUserDetails) authentication.getPrincipal();
                User authenticatedUser = authenticatedUserDetails.getUser();

                String accessToken = jwtUtil.createJwt("access", authenticatedUser.getUserId(), authenticatedUser.getRole(), 600000L);
                String refreshToken = jwtUtil.createJwt("refresh", authenticatedUser.getUserId(), authenticatedUser.getRole(), 86400000L);

                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + accessToken)
                        .header("Set-Cookie", "refresh=" + refreshToken + "; HttpOnly; Path=/; Max-Age=86400000;")
                        .body(authenticatedUser);
            } else {
                user.setPwdCheckCount(user.getPwdCheckCount() + 1);
                userRepository.save(user);
                logger.warning("비밀번호나 접속번호가 일치하지 않음 (관리자)");
            }
        } else {
            logger.warning("관리자를 찾을 수 없음: " + adminId);
        }

        return ResponseEntity.status(401).build();
    }

    @Override
    public ResponseEntity<User> frOwnerLogin(String frOwnerId, String frOwnerPassword) {
        Optional<User> optionalUser = userRepository.findByUserId(frOwnerId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPwdCheckCount() >= 5) {
                logger.warning("비밀번호 틀린 횟수 초과: " + frOwnerId);
                return ResponseEntity.status(403).body(user);
            }

            if ("ROLE_OWNER".equals(user.getRole()) && passwordEncoder.matches(frOwnerPassword, user.getPassword())) {

                // 로그인 성공, 비밀번호 틀린 횟수 초기화
                user.setPwdCheckCount(0);



                userRepository.save(user);

                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(frOwnerId, frOwnerPassword));
                CustomUserDetails authenticatedUserDetails = (CustomUserDetails) authentication.getPrincipal();
                User authenticatedUser = authenticatedUserDetails.getUser();

                String accessToken = jwtUtil.createJwt("access", authenticatedUser.getUserId(), authenticatedUser.getRole(), 600000L);
                String refreshToken = jwtUtil.createJwt("refresh", authenticatedUser.getUserId(), authenticatedUser.getRole(), 86400000L);

                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + accessToken)
                        .header("Set-Cookie", "refresh=" + refreshToken + "; HttpOnly; Path=/; Max-Age=86400000;")
                        .body(authenticatedUser);
            } else {
                user.setPwdCheckCount(user.getPwdCheckCount() + 1);
                userRepository.save(user);
                logger.warning("비밀번호가 일치하지 않음 (점주)");
            }
        } else {
            logger.warning("점주를 찾을 수 없음: " + frOwnerId);
        }

        return ResponseEntity.status(401).build();
    }

    @Override
    public ResponseEntity<User> driverLogin(String driverId, String driverPassword) {
        Optional<User> optionalUser = userRepository.findByUserId(driverId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPwdCheckCount() >= 5) {
                logger.warning("비밀번호 틀린 횟수 초과: " + driverId);
                return ResponseEntity.status(403).body(user);
            }

            if ("ROLE_DRIVER".equals(user.getRole()) && passwordEncoder.matches(driverPassword, user.getPassword())) {

                // 로그인 성공, 비밀번호 틀린 횟수 초기화
                user.setPwdCheckCount(0);



                userRepository.save(user);

                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(driverId, driverPassword));
                CustomUserDetails authenticatedUserDetails = (CustomUserDetails) authentication.getPrincipal();
                User authenticatedUser = authenticatedUserDetails.getUser();

                String accessToken = jwtUtil.createJwt("access", authenticatedUser.getUserId(), authenticatedUser.getRole(), 600000L);
                String refreshToken = jwtUtil.createJwt("refresh", authenticatedUser.getUserId(), authenticatedUser.getRole(), 86400000L);

                return ResponseEntity.ok()
                        .header("Authorization", "Bearer " + accessToken)
                        .header("Set-Cookie", "refresh=" + refreshToken + "; HttpOnly; Path=/; Max-Age=86400000;")
                        .body(authenticatedUser);
            } else {
                user.setPwdCheckCount(user.getPwdCheckCount() + 1);
                userRepository.save(user);
                logger.warning("비밀번호가 일치하지 않음 (배송기사)");
            }
        } else {
            logger.warning("배송기사를 찾을 수 없음: " + driverId);
        }

        return ResponseEntity.status(401).build();
    }
}
