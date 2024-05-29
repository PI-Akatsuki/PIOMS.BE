package com.akatsuki.pioms.config;

import com.akatsuki.pioms.jwt.JWTFilter;
import com.akatsuki.pioms.jwt.JWTUtil;
import com.akatsuki.pioms.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Autowired
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }


    // AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable());


        http
                .authorizeHttpRequests(authorize -> authorize
                                .anyRequest().permitAll()
//                        .requestMatchers("/reissue", "/login", "/admin/login", "/franchise/login", "/driver/login").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ROOT")
//                        .requestMatchers(
//                                "/admin/info",
//                                "/admin/list/**",
//                                "/admin/category/first/list/**",
//                                "/admin/category/second/list/**",
//                                "/admin/category/third/list/**",
//                                "/admin/driver/list/**",
//                                "/admin/franchise/list/**",
//                                "/admin/franchise/owner/list/**",
//                                "/admin/franchise/owner/update/**",
//                                "/admin/product/list/**",
//                                "/admin/specs/**",
//                                "/admin/order/**",
//                                "/admin/invoice/**",
//                                "/admin/exchange/**",
//                                "/admin/notice/list/**",
//                                "/admin/ask/**",
//                                "/admin/exceldownload/**").hasRole("ADMIN")
//                        .requestMatchers("/franchise/**").hasRole("OWNER")
//                        .requestMatchers("/driver/**").hasRole("DRIVER")
//                        .anyRequest().authenticated()
                );
        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // Session 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

//                .sessionManagement(session -> session
//                        .maximumSessions(5)
//                        .maxSessionsPreventsLogin(true)
//                )
//                .sessionManagement(session -> session
//                        .sessionFixation().changeSessionId()
//                );
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(Collections.singletonList("http://localhost:8080"));
//        configuration.setAllowedOriginPatterns(Collections.singletonList("http://api.pioms.shop"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
//        configuration.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

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
