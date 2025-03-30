package com.fruitmall.global.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

    /**
     * 현재 인증된 사용자의 ID(username)를 가져옵니다.
     * 주로 회원의 username 또는 이메일을 반환합니다.
     * 
     * @return 현재 인증된 사용자의 ID를 Optional로 반환
     */
    public static Optional<String> getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return Optional.ofNullable(username);
    }
    
    /**
     * 현재 인증된 사용자가 인증되었는지 확인합니다.
     * 
     * @return 인증 여부
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }
    
    /**
     * 현재 인증된 사용자가 지정된 역할을 가지고 있는지 확인합니다.
     * 
     * @param role 확인할 역할 (예: "ROLE_USER", "ROLE_ADMIN")
     * @return 역할 포함 여부
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.getAuthorities().stream()
                   .anyMatch(authority -> authority.getAuthority().equals(role));
    }
    
    /**
     * 현재 인증된 사용자가 관리자 역할을 가지고 있는지 확인합니다.
     * 
     * @return 관리자 여부
     */
    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
    
    /**
     * 현재 인증 객체를 가져옵니다.
     * 
     * @return 현재 인증 객체를 Optional로 반환
     */
    public static Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }
}