package com.fruitmall.global.config;

import com.fruitmall.global.security.jwt.JwtAuthenticationFilter;
import com.fruitmall.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Swagger/OpenAPI 엔드포인트
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // 액추에이터 엔드포인트
                .requestMatchers("/actuator/**").permitAll()
                // 인증 관련 엔드포인트
                .requestMatchers("/auth/**").permitAll()
                // 과일 상품 조회 (사용자 권한 불필요)
                .requestMatchers(
                    "/api/fruits/**",
                    "/api/categories/**").permitAll()
                // 회원 관련 엔드포인트
                .requestMatchers("/members/**").authenticated()
                // 주문 관련 엔드포인트
                .requestMatchers("/orders/**").authenticated()
                // 리뷰 관련 엔드포인트
                .requestMatchers("/reviews/**").authenticated()
                // 장바구니 관련 엔드포인트
                .requestMatchers("/cart/**").authenticated()
                // 찜 목록 관련 엔드포인트
                .requestMatchers("/wishlist/**").authenticated()
                // 관리자 전용 엔드포인트
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}