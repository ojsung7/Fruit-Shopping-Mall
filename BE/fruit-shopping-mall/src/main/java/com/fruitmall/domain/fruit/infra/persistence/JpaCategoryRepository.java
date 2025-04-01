package com.fruitmall.domain.fruit.infra.persistence;

import com.fruitmall.domain.fruit.domain.Category;
import com.fruitmall.domain.fruit.domain.CategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaCategoryRepository extends JpaRepository<Category, Long>, CategoryRepository {
    
    Optional<Category> findByName(String name);
    
    boolean existsByName(String name);
}