package com.fruitmall.domain.fruit.domain;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    
    Category save(Category category);
    
    Optional<Category> findById(Long id);
    
    Optional<Category> findByName(String name);
    
    List<Category> findAll();
    
    boolean existsById(Long id);
    
    boolean existsByName(String name);
    
    void delete(Category category);
    
    long count();
}