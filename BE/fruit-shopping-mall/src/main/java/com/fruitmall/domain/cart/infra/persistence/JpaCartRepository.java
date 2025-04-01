package com.fruitmall.domain.cart.infra.persistence;

import com.fruitmall.domain.cart.domain.Cart;
import com.fruitmall.domain.cart.domain.CartRepository;
import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCartRepository extends JpaRepository<Cart, Long>, CartRepository {
    
    List<Cart> findByMember(Member member);
    
    Optional<Cart> findByMemberAndFruit(Member member, Fruit fruit);
    
    boolean existsByMemberAndFruit(Member member, Fruit fruit);
    
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.member = :member")
    void deleteByMember(@Param("member") Member member);
    
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.member = :member AND c.fruit = :fruit")
    void deleteByMemberAndFruit(@Param("member") Member member, @Param("fruit") Fruit fruit);
    
    int countByMember(Member member);
}