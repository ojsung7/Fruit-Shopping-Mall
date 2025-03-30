package com.fruitmall.application.auth;

import com.fruitmall.application.auth.dto.LoginRequestDto;
import com.fruitmall.application.auth.dto.TokenDto;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.member.domain.MemberRepository;
import com.fruitmall.global.error.BusinessException;
import com.fruitmall.global.error.ErrorCode;
import com.fruitmall.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public TokenDto login(LoginRequestDto request) {
        // 이메일 또는 사용자 이름으로 회원 조회
        Optional<Member> memberByUsername = memberRepository.findByUsername(request.getUsernameOrEmail());
        Optional<Member> memberByEmail = memberRepository.findByEmail(request.getUsernameOrEmail());
        
        Member member = memberByUsername.orElseGet(() -> 
                memberByEmail.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND)));
        
        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
        
        // Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(member.getUsername(), request.getPassword());
        
        // 실제 검증 (사용자 비밀번호 체크)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        
        // SecurityContext에 Authentication 객체를 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 토큰 생성
        String jwt = tokenProvider.createToken(authentication);
        
        return TokenDto.builder()
                .token(jwt)
                .tokenType("Bearer")
                .expiresIn(86400000L) // 24시간 (밀리초 단위)
                .build();
    }
}