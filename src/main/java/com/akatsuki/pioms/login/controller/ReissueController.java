package com.akatsuki.pioms.login.controller;

import com.akatsuki.pioms.jwt.JWTUtil;
import com.akatsuki.pioms.login.service.RedisTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@Tag(name = "[시스템]토큰관리 API")
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
        String refreshToken = extractRefreshTokenFromCookies(request.getCookies());

        if (refreshToken == null) {
            logger.warning("Refresh token is null");
            return new ResponseEntity<>("Refresh token is null", HttpStatus.BAD_REQUEST);
        }

        if (isTokenExpired(refreshToken)) {
            logger.warning("Refresh token is expired");
            return new ResponseEntity<>("Refresh token is expired", HttpStatus.UNAUTHORIZED);
        }

        if (!isRefreshToken(refreshToken)) {
            logger.warning("Invalid refresh token category");
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String userId = jwtUtil.getUserId(refreshToken);
        if (!isValidRefreshToken(refreshToken, userId)) {
            logger.warning("Refresh token does not match or not found in Redis");
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        String newAccessToken = createNewAccessToken(refreshToken);
        response.setHeader("Authorization", "Bearer " + newAccessToken);

        return ResponseEntity.ok().body("Bearer " + newAccessToken);
    }

    private String extractRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private boolean isTokenExpired(String token) {
        try {
            jwtUtil.isExpired(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private boolean isRefreshToken(String token) {
        String category = jwtUtil.getCategory(token);
        return "refresh".equals(category);
    }

    private boolean isValidRefreshToken(String token, String userId) {
        String storedRefreshToken = redisTokenService.getRefreshToken(userId);
        return storedRefreshToken != null && storedRefreshToken.equals(token);
    }

    private String createNewAccessToken(String refreshToken) {
        int userCode = jwtUtil.getUserCode(refreshToken);
        String username = jwtUtil.getUsername(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        return jwtUtil.createJwt("access", userCode, jwtUtil.getUserId(refreshToken), username, role, 600000L); // 10분 유효
    }
}
