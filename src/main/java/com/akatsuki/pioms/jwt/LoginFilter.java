package com.akatsuki.pioms.jwt;

import com.akatsuki.pioms.user.dto.CustomUserDetails;
import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("username = " + username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Object user = userDetails.getUser();
        int usercode;
        String userid;

        if (user instanceof Admin) {
            usercode = ((Admin) user).getAdminCode();
            userid = ((Admin) user).getAdminId();
        } else if (user instanceof FranchiseOwner) {
            usercode = ((FranchiseOwner) user).getFranchiseOwnerCode();
            userid = ((FranchiseOwner) user).getFranchiseOwnerId();
        } else {
            usercode = ((DeliveryDriver) user).getDriverCode();
            userid = ((DeliveryDriver) user).getDriverId();
        }

        String access = jwtUtil.createJwt("access", usercode, userid, username, role, 86400000L);
        String refresh = jwtUtil.createJwt("refresh", usercode, userid, username, role, 864000000L);

        response.setHeader("Authorization", "Bearer " + access);

        response.addCookie(createCookie("refreshToken", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
        System.out.println("fail");
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(86400);  // 86400 seconds = 1 day
        cookie.setPath("/");
//        cookie.setSecure(true);   // https를 위함
        cookie.setHttpOnly(true);
        return cookie;
    }
}
