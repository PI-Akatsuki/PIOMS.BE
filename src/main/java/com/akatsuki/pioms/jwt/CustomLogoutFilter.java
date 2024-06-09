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
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestUri = httpRequest.getRequestURI();
        logger.info("Received request URI: " + requestUri);

        if (!"/logout".equals(requestUri)) {
            chain.doFilter(request, response);
            return;
        }

        if (!"POST".equals(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh".equals(cookie.getName())) {
                    refresh = cookie.getValue();
                    logger.info("Found refresh token in cookies: " + refresh);
                }
            }
        }

        if (refresh == null) {
            logger.warning("No refresh token found in cookies");
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            logger.warning("Refresh token is expired");
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String category = jwtUtil.getCategory(refresh);
        logger.info("Token category: " + category);
        if (!"refresh".equals(category)) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String userId = jwtUtil.getUserId(refresh);
        boolean isExist = redisTokenService.getRefreshToken(userId) != null;
        if (!isExist) {
            logger.warning("No refresh token found in Redis for user: " + userId);
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Redis에서 Refresh 지우는거
//        redisTokenService.deleteRefreshToken(userId);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpResponse.addCookie(cookie);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        httpResponse.getWriter().write("Logout successful");
        logger.info("Logout successful for user: " + userId);
    }
}
