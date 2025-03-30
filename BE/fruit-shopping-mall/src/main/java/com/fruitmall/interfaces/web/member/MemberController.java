package com.fruitmall.interfaces.web.member;

import com.fruitmall.domain.member.application.MemberService;
import com.fruitmall.domain.member.application.dto.ChangePasswordDto;
import com.fruitmall.domain.member.application.dto.MemberDto;
import com.fruitmall.domain.member.application.dto.MemberRegisterDto;
import com.fruitmall.domain.member.application.dto.MemberUpdateDto;
import com.fruitmall.global.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "회원 관리", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 등록", description = "새로운 회원을 등록합니다")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberDto> register(@Valid @RequestBody MemberRegisterDto dto) {
        MemberDto memberDto = memberService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberDto);
    }

    @Operation(summary = "회원 정보 조회", description = "회원 ID로 회원 정보를 조회합니다")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.getCurrentUsername().equals(#id)")
    public ResponseEntity<MemberDto> getMember(@PathVariable Long id) {
        MemberDto memberDto = memberService.findById(id);
        return ResponseEntity.ok(memberDto);
    }

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 회원의 정보를 조회합니다")
    @GetMapping("/me")
    public ResponseEntity<MemberDto> getMyInfo() {
        String username = SecurityUtil.getCurrentUsername()
                .orElseThrow(() -> new IllegalStateException("로그인 유저 정보가 없습니다"));
        MemberDto memberDto = memberService.findByUsername(username);
        return ResponseEntity.ok(memberDto);
    }

    @Operation(summary = "전체 회원 목록 조회", description = "모든 회원 정보를 조회합니다 (관리자 전용)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MemberDto>> getAllMembers() {
        List<MemberDto> members = memberService.findAll();
        return ResponseEntity.ok(members);
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.getCurrentUsername().equals(#id)")
    public ResponseEntity<MemberDto> updateMember(@PathVariable Long id, @Valid @RequestBody MemberUpdateDto dto) {
        MemberDto updatedMember = memberService.update(id, dto);
        return ResponseEntity.ok(updatedMember);
    }

    @Operation(summary = "비밀번호 변경", description = "회원의 비밀번호를 변경합니다")
    @PutMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.getCurrentUsername().equals(#id)")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordDto dto) {
        memberService.changePassword(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "관리자 권한 부여", description = "회원에게 관리자 권한을 부여합니다 (관리자 전용)")
    @PutMapping("/{id}/grant-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MemberDto> grantAdminRole(@PathVariable Long id) {
        MemberDto updatedMember = memberService.grantAdminRole(id);
        return ResponseEntity.ok(updatedMember);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 정보를 삭제합니다")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.getCurrentUsername().equals(#id)")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}