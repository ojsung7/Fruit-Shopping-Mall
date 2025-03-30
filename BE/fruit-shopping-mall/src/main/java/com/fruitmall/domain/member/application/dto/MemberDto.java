package com.fruitmall.domain.member.application.dto;

import com.fruitmall.domain.member.domain.MemberGrade;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class MemberDto {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String phoneNumber;
    private LocalDate birthDate;
    private String address;
    private LocalDate joinDate;
    private MemberGrade memberGrade;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}