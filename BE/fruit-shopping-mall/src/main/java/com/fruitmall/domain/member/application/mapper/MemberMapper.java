package com.fruitmall.domain.member.application.mapper;

import com.fruitmall.domain.member.application.dto.MemberDto;
import com.fruitmall.domain.member.application.dto.MemberRegisterDto;
import com.fruitmall.domain.member.domain.Member;

// MapStruct 어노테이션 제거
public interface MemberMapper {

    // 메서드 시그니처만 남김 (어노테이션 제거)
    Member toEntity(MemberRegisterDto dto);

    MemberDto toDto(Member member);
}