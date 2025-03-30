package com.fruitmall.application.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "아이디 또는 이메일은 필수 입력값입니다")
    private String usernameOrEmail;

    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    private String password;
}