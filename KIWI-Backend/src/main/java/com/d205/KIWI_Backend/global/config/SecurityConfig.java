package com.d205.KIWI_Backend.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint entryPoint;

    private final String[] allowedUrls = {
            "/",
            "/swagger-ui/**",
            "/v3/**",
            "/members/register",
            "/members/sign-in",
            "/actuator/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(allowedUrls).permitAll()
                        // H2 Console 관련 설정 제거
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}