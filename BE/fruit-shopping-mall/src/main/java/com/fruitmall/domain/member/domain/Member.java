package com.fruitmall.domain.member.domain;

import com.fruitmall.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String phoneNumber;

    private LocalDate birthDate;

    private String address;

    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    private MemberGrade memberGrade;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    @Builder(toBuilder = true)  // toBuilder 옵션 추가
    public Member(String username, String email, String password, String name, 
                 String phoneNumber, LocalDate birthDate, String address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = address;
        this.joinDate = LocalDate.now();
        this.memberGrade = MemberGrade.BRONZE;
        this.roles = new HashSet<>();
        this.roles.add("ROLE_USER");
    }
    // 비밀번호 암호화
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    // 비밀번호 변경
    public void changePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    // 회원 정보 업데이트
    public void updateInfo(String name, String phoneNumber, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // 회원 등급 업데이트
    public void updateGrade(MemberGrade grade) {
        this.memberGrade = grade;
    }

    // 관리자 권한 추가
    public void addAdminRole() {
        this.roles.add("ROLE_ADMIN");
    }

    // 패스워드 확인
    public boolean matchPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }
}