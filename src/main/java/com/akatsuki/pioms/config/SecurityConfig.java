package com.akatsuki.pioms.config;

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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorize) -> authorize
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
                                "/admin/exceldownload/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .defaultSuccessUrl("/admin/home", true)
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/franchise/login")
                        .defaultSuccessUrl("/franchise/home", true)
                        .failureUrl("/franchise/login?error=true")
                        .permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/driver/login")
                        .defaultSuccessUrl("/driver/home", true)
                        .failureUrl("/driver/login?error=true")
                        .permitAll()
                );

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());

        http
                .logout((auth) -> auth
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true"));

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

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("root")
                .password(passwordEncoder().encode("password"))
                .roles("ROOT")
                .build());
        manager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("password"))
                .roles("ADMIN")
                .build());
        manager.createUser(User.withUsername("owner")
                .password(passwordEncoder().encode("password"))
                .roles("OWNER")
                .build());
        return manager;
    }
}
