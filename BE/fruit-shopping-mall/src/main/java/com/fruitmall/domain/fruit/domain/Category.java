package com.fruitmall.domain.fruit.domain;

import com.fruitmall.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "category")
    private List<Fruit> fruits = new ArrayList<>();

    @Builder
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // 카테고리 정보 업데이트
    public void updateInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }
}