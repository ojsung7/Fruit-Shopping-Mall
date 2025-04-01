package com.fruitmall.domain.cart.domain;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface CartRepository {
    
    Cart save(Cart cart);
    
    Optional<Cart> findById(Long id);
    
    List<Cart> findByMember(Member member);
    
    Optional<Cart> findByMemberAndFruit(Member member, Fruit fruit);
    
    boolean existsByMemberAndFruit(Member member, Fruit fruit);
    
    void delete(Cart cart);
    
    void deleteByMember(Member member);
    
    void deleteByMemberAndFruit(Member member, Fruit fruit);
    
    int countByMember(Member member);
}