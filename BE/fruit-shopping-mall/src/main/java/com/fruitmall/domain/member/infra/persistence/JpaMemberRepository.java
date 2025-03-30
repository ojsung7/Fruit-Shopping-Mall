package com.fruitmall.domain.member.infra.persistence;

import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.member.domain.MemberGrade;
import com.fruitmall.domain.member.domain.MemberRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<Member> findByMemberGrade(MemberGrade grade);
    List<Member> findByJoinDateBetween(LocalDate from, LocalDate to);
}