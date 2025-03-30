package com.fruitmall.domain.member.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<Member> findAll();
    List<Member> findByMemberGrade(MemberGrade grade);
    List<Member> findByJoinDateBetween(LocalDate from, LocalDate to);
    void delete(Member member);
}