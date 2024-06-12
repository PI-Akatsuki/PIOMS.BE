package com.akatsuki.pioms.config;

import com.akatsuki.pioms.jwt.CustomLogoutFilter;
import com.akatsuki.pioms.jwt.JWTFilter;
import com.akatsuki.pioms.jwt.JWTUtil;
import com.akatsuki.pioms.jwt.LoginFilter;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import com.akatsuki.pioms.login.service.RedisTokenService;
import com.akatsuki.pioms.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
    private final AdminRepository adminRepository;
    private final FranchiseOwnerRepository franchiseOwnerRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(
            AuthenticationConfiguration authenticationConfiguration,
            JWTUtil jwtUtil,
            RedisTokenService redisTokenService,
            AdminRepository adminRepository,
            FranchiseOwnerRepository franchiseOwnerRepository,
            DeliveryDriverRepository deliveryDriverRepository,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.redisTokenService = redisTokenService;
        this.adminRepository = adminRepository;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
        this.deliveryDriverRepository = deliveryDriverRepository;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/actuator/prometheus","/swagger-ui/**", "/v3/api-docs/**", "/reissue", "/admin/login", "/franchise/login", "/driver/login", "/admin/product/sendKakaoAlert").permitAll()
                        .requestMatchers(
                                "/admin/info",
                                "/admin/home",
                                "/admin/list/**",
                                "/admin/product/**",
                                "/admin/category/**",
                                "/admin/driver/list/**",
                                "/admin/franchise/list/**",
                                "/admin/franchise/owner/list/**",
                                "/admin/franchise/**",
                                "/admin/product/list/**",
                                "/admin/specs/**",
                                "/admin/order/**",
                                "/admin/invoice/**",
                                "/admin/exchange/**",
                                "/admin/notice/list/**",
                                "/admin/ask/**",
                                "/admin/pdfdownload/**",
                                "/admin/exceldownload/**",
                                "/admin/adminDashboard",
                                "/admin/pdfdownload/admin-pdf").hasAnyRole("ADMIN", "ROOT")
                        .requestMatchers("/admin/**").hasRole("ROOT")
                        .requestMatchers("/franchise/**").hasRole("OWNER")
                        .requestMatchers("/driver/**").hasRole("DRIVER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, redisTokenService), LogoutFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil, adminRepository, franchiseOwnerRepository, deliveryDriverRepository), UsernamePasswordAuthenticationFilter.class)
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
        String hierarchy = "ROLE_ROOT > ROLE_ADMIN\nROLE_ADMIN > ROLE_OWNER\nROLE_ADMIN > ROLE_DRIVER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }
}
