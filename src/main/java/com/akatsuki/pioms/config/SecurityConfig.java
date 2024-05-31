package com.akatsuki.pioms.config;

import com.akatsuki.pioms.jwt.CustomLogoutFilter;
import com.akatsuki.pioms.jwt.JWTFilter;
import com.akatsuki.pioms.jwt.JWTUtil;
import com.akatsuki.pioms.jwt.LoginFilter;
import com.akatsuki.pioms.redis.RedisTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RedisTokenService redisTokenService;

    @Autowired
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RedisTokenService redisTokenService) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.redisTokenService = redisTokenService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/reissue", "/admin/login", "/franchise/login", "/driver/login").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ROOT")
                                .requestMatchers("/admin/**").hasAnyRole("ROOT","ADMIN")
                                .requestMatchers(
                                        "/admin/info",
                                        "/admin/home",
                                        "/admin/list/**",
                                        "/admin/category/first/list/**",
                                        "/admin/category/second/list/**",
                                        "/admin/category/third/list/**",
                                        "/admin/driver/list/**",
                                        "/admin/franchise/list/**",
                                        "/admin/franchise/owner/list/**",
                                        "/admin/franchise/owner/update/**",
                                        "/admin/product/list/**",
                                        "/admin/specs/**",
                                        "/admin/order/**",
                                        "/admin/invoice/**",
                                        "/admin/exchange/**",
                                        "/admin/notice/list/**",
                                        "/admin/ask/**",
                                        "/admin/exceldownload/**").hasRole("ROOT")
                                .requestMatchers("/franchise/**").hasRole("ROOT")
                                .requestMatchers("/driver/**").hasAnyRole("ROOT", "DRIVER")
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, redisTokenService), LogoutFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ROOT > ROLE_ADMIN\nROLE_ADMIN > ROLE_OWNER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
}
