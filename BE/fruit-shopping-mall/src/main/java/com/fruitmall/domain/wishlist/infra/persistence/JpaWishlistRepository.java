package com.fruitmall.domain.wishlist.infra.persistence;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.domain.wishlist.domain.Wishlist;
import com.fruitmall.domain.wishlist.domain.WishlistRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaWishlistRepository extends JpaRepository<Wishlist, Long>, WishlistRepository {
    
    List<Wishlist> findByMember(Member member);
    
    Optional<Wishlist> findByMemberAndFruit(Member member, Fruit fruit);
    
    boolean existsByMemberAndFruit(Member member, Fruit fruit);
    
    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.member = :member")
    void deleteByMember(@Param("member") Member member);
    
    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.member = :member AND w.fruit = :fruit")
    void deleteByMemberAndFruit(@Param("member") Member member, @Param("fruit") Fruit fruit);
    
    int countByMember(Member member);
}