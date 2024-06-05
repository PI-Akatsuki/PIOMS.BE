package com.akatsuki.pioms.jwt;

import com.akatsuki.pioms.user.dto.CustomUserDetails;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final AdminRepository adminRepository;
    private final FranchiseOwnerRepository franchiseOwnerRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;

    public JWTFilter(JWTUtil jwtUtil, AdminRepository adminRepository, FranchiseOwnerRepository franchiseOwnerRepository, DeliveryDriverRepository deliveryDriverRepository) {
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
        this.deliveryDriverRepository = deliveryDriverRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorizationHeader.substring(7);

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");
            return;
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!"access".equals(category)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");
            return;
        }

        String userId = jwtUtil.getUserId(accessToken);
        CustomUserDetails userDetails = null;

        if ("ROLE_ROOT".equals(jwtUtil.getRole(accessToken)) || "ROLE_ADMIN".equals(jwtUtil.getRole(accessToken))) {
            userDetails = new CustomUserDetails(adminRepository.findByAdminId(userId).orElseThrow(), jwtUtil.getAuthorities(accessToken));
        } else if ("ROLE_OWNER".equals(jwtUtil.getRole(accessToken))) {
            userDetails = new CustomUserDetails(franchiseOwnerRepository.findByFranchiseOwnerId(userId).orElseThrow(), jwtUtil.getAuthorities(accessToken));
        } else if ("ROLE_DRIVER".equals(jwtUtil.getRole(accessToken))) {
            userDetails = new CustomUserDetails(deliveryDriverRepository.findByDriverId(userId).orElseThrow(), jwtUtil.getAuthorities(accessToken));
        }

        if (userDetails != null) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
