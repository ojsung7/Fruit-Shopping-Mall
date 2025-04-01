package com.fruitmall.domain.wishlist.domain;

import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.member.domain.Member;
import com.fruitmall.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "wishlist", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"member_id", "fruit_id"})
       })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fruit_id")
    private Fruit fruit;

    @Column(nullable = false)
    private LocalDate addedDate;

    @Builder
    public Wishlist(Member member, Fruit fruit) {
        this.member = member;
        this.fruit = fruit;
        this.addedDate = LocalDate.now();
    }
}