package com.akatsuki.pioms.login.controller;

import com.akatsuki.pioms.jwt.JWTUtil;
import com.akatsuki.pioms.login.service.RedisTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class ReissueController {

    private static final Logger logger = Logger.getLogger(ReissueController.class.getName());

    private final JWTUtil jwtUtil;
    private final RedisTokenService redisTokenService;

    public ReissueController(JWTUtil jwtUtil, RedisTokenService redisTokenService) {
        this.jwtUtil = jwtUtil;
        this.redisTokenService = redisTokenService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        // get refresh token
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        if (refreshToken == null) {
            logger.warning("Refresh token is null");
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        // expired check
        try {
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            logger.warning("Refresh token is expired");
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refreshToken);
        if (!"refresh".equals(category)) {
            logger.warning("Invalid refresh token category");
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // Redis에서 refresh 토큰 확인
        String userId = jwtUtil.getUserId(refreshToken);
        String storedRefreshToken = redisTokenService.getRefreshToken(userId);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            logger.warning("Refresh token does not match or not found in Redis");
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 필요한 정보 추출
        int userCode = jwtUtil.getUserCode(refreshToken);
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // make new JWT
        String newAccessToken = jwtUtil.createJwt("access", userCode, userId, username, role, 600000L); // 10분 유효

        // response
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
