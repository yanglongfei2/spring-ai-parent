package com.longfei.yang.study.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        // 创建用户 "user1" 并分配角色 "USER"
        UserDetails user = User.withUsername("user1")
                               .password("pass1")
                               .roles("USER")
                               .build();

        // 创建用户 "admin" 并分配角色 "ADMIN"
        UserDetails admin = User.withUsername("admin")
                                .password("admin")
                                .roles("ADMIN")
                                .build();

        // 返回一个使用内存存储用户信息的 UserDetailsService 实例
        return new InMemoryUserDetailsManager(user, admin);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/tool").permitAll()
                        .requestMatchers("/send").permitAll()  // 允许 NL2SQL 接口无需认证
                        .requestMatchers("/nl2sql-chat.html").permitAll()  // 允许前端页面访问
                        .requestMatchers("/static/**").permitAll()  // 允许静态资源访问
                        .requestMatchers("/*.html").permitAll()  // 允许所有 HTML 页面访问
                        .anyRequest().authenticated()
                )
                .with(new FormLoginConfigurer<>(), Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}