package com.fruitmall.domain.member.application;

import com.fruitmall.domain.member.application.dto.ChangePasswordDto;
import com.fruitmall.domain.member.application.dto.MemberDto;
import com.fruitmall.domain.member.application.dto.MemberRegisterDto;
import com.fruitmall.domain.member.application.dto.MemberUpdateDto;
import com.fruitmall.domain.member.application.mapper.MemberMapper;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.member.domain.MemberRepository;
import com.fruitmall.global.error.BusinessException;
import com.fruitmall.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    // 회원 등록
    @Transactional
    public MemberDto register(MemberRegisterDto dto) {
        // 중복 확인
        if (memberRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessException(ErrorCode.USERNAME_DUPLICATION);
        }
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }

        // 엔티티 변환
        Member member = memberMapper.toEntity(dto);
        
        // 비밀번호 암호화
        member.encodePassword(passwordEncoder);
        
        // 저장
        Member savedMember = memberRepository.save(member);
        
        return memberMapper.toDto(savedMember);
    }

    // 회원 정보 조회
    public MemberDto findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return memberMapper.toDto(member);
    }

    // 사용자 이름으로 회원 정보 조회
    public MemberDto findByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return memberMapper.toDto(member);
    }

    // 이메일로 회원 정보 조회
    public MemberDto findByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return memberMapper.toDto(member);
    }

    // 모든 회원 조회
    public List<MemberDto> findAll() {
        return memberRepository.findAll().stream()
                .map(memberMapper::toDto)
                .collect(Collectors.toList());
    }

    // 회원 정보 수정
    @Transactional
    public MemberDto update(Long id, MemberUpdateDto dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        member.updateInfo(dto.getName(), dto.getPhoneNumber(), dto.getAddress());
        
        return memberMapper.toDto(member);
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long id, ChangePasswordDto dto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        // 현재 비밀번호 확인
        if (!member.matchPassword(dto.getCurrentPassword(), passwordEncoder)) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
        
        // 비밀번호 변경
        member.changePassword(dto.getNewPassword(), passwordEncoder);
    }

    // 관리자 권한 부여
    @Transactional
    public MemberDto grantAdminRole(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        member.addAdminRole();
        
        return memberMapper.toDto(member);
    }

    // 회원 탈퇴
    @Transactional
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        
        memberRepository.delete(member);
    }
}