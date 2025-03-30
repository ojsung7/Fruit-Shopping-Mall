package com.fruitmall.domain.member.application.mapper;

import com.fruitmall.domain.member.application.dto.MemberDto;
import com.fruitmall.domain.member.application.dto.MemberRegisterDto;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.member.domain.MemberGrade;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;

@Component  // Spring Bean으로 등록
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member toEntity(MemberRegisterDto dto) {
        if (dto == null) {
            return null;
        }
        
        // Member.builder()를 사용하여 엔티티 생성
        return Member.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .name(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .build();
        
        // 참고: joinDate, memberGrade, roles는 Member 생성자에서 초기화되므로 여기서 설정할 필요 없음
    }

    @Override
    public MemberDto toDto(Member member) {
        if (member == null) {
            return null;
        }
        
        // MemberDto.builder()를 사용하여 DTO 생성
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .birthDate(member.getBirthDate())
                .address(member.getAddress())
                .joinDate(member.getJoinDate())
                .memberGrade(member.getMemberGrade())
                .roles(new HashSet<>(member.getRoles()))  // Set 복사
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}