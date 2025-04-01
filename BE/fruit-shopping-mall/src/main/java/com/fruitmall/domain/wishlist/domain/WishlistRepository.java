package com.fruitmall.domain.wishlist.domain;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository {
    
    Wishlist save(Wishlist wishlist);
    
    Optional<Wishlist> findById(Long id);
    
    List<Wishlist> findByMember(Member member);
    
    Optional<Wishlist> findByMemberAndFruit(Member member, Fruit fruit);
    
    boolean existsByMemberAndFruit(Member member, Fruit fruit);
    
    void delete(Wishlist wishlist);
    
    void deleteByMember(Member member);
    
    void deleteByMemberAndFruit(Member member, Fruit fruit);
    
    int countByMember(Member member);
}