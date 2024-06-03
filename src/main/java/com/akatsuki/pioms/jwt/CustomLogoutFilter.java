package com.akatsuki.pioms.jwt;

import com.akatsuki.pioms.login.service.RedisTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.logging.Logger;

public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RedisTokenService redisTokenService;
    private static final Logger logger = Logger.getLogger(CustomLogoutFilter.class.getName());

    public CustomLogoutFilter(JWTUtil jwtUtil, RedisTokenService redisTokenService) {
        this.jwtUtil = jwtUtil;
        this.redisTokenService = redisTokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        logger.info("Received request URI: " + requestUri); // 로그 추가

        // /admin/product/sendKakaoAlert 요청을 제외
        if (!requestUri.matches("^\\/logout$") && !"/admin/product/sendKakaoAlert".equals(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("/admin/product/sendKakaoAlert".equals(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestMethod = request.getMethod();
        logger.info("Request method: " + requestMethod); // 로그 추가
        if (!"POST".equals(requestMethod)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    logger.info("Found refresh token in cookies: " + refresh); // 로그 추가
                }
            }
        }

        if (refresh == null) {
            logger.warning("No refresh token found in cookies");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            logger.warning("Refresh token is expired");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String category = jwtUtil.getCategory(refresh);
        logger.info("Token category: " + category); // 로그 추가
        if (!"refresh".equals(category)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String userId = jwtUtil.getUserId(refresh);
        boolean isExist = redisTokenService.getRefreshToken(userId) != null;
        if (!isExist) {
            logger.warning("No refresh token found in Redis for user: " + userId); // 로그 추가
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        redisTokenService.deleteRefreshToken(userId);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
        logger.info("Logout successful for user: " + userId); // 로그 추가
    }
}
