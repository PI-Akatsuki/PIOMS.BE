package com.akatsuki.pioms.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/admin/login", "/franchise/login", "/driver/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ROOT")
                        .requestMatchers(
                                "/admin/info",
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
                                "/admin/exceldownload/**").hasRole("ADMIN")
                        .requestMatchers("/franchise/**").hasRole("OWNER")
                        .requestMatchers("/driver/**").hasRole("DRIVER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/home", true)
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/franchise/login")
                        .loginProcessingUrl("/franchise/login")
                        .defaultSuccessUrl("/franchise/home", true)
                        .failureUrl("/franchise/login?error=true")
                        .permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/driver/login")
                        .loginProcessingUrl("/driver/login")
                        .defaultSuccessUrl("/driver/home", true)
                        .failureUrl("/driver/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                )
                .sessionManagement(session -> session
                        .sessionFixation().changeSessionId()
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager user = new InMemoryUserDetailsManager();
        user.createUser(User.withUsername("root")
                .password(passwordEncoder().encode("password"))
                .roles("ROOT")
                .build());
        user.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN")
                .build());
        user.createUser(User.withUsername("owner")
                .password(passwordEncoder().encode("password"))
                .roles("OWNER")
                .build());
        user.createUser(User.withUsername("driver")
                .password(passwordEncoder().encode("password"))
                .roles("DRIVER")
                .build());
        return user;
    }
}
