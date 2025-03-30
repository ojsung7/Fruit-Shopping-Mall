package com.fruitmall.interfaces.web.auth;

import com.fruitmall.application.auth.AuthService;
import com.fruitmall.application.auth.dto.LoginRequestDto;
import com.fruitmall.application.auth.dto.TokenDto;
import com.fruitmall.domain.member.application.MemberService;
import com.fruitmall.domain.member.application.dto.MemberDto;
import com.fruitmall.domain.member.application.dto.MemberRegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "로그인 및 회원가입 관련 API")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    @Operation(summary = "로그인", description = "이메일/아이디와 비밀번호로 로그인하여 JWT 토큰을 발급받습니다")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequestDto request) {
        TokenDto tokenDto = authService.login(request);
        return ResponseEntity.ok(tokenDto);
    }

    @Operation(summary = "회원가입", description = "신규 회원을 등록합니다")
    @PostMapping("/register")
    public ResponseEntity<MemberDto> register(@Valid @RequestBody MemberRegisterDto request) {
        MemberDto memberDto = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberDto);
    }
}