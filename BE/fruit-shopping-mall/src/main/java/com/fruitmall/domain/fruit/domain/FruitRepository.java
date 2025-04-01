package com.fruitmall.domain.fruit.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FruitRepository {
    
    Fruit save(Fruit fruit);
    
    Optional<Fruit> findById(Long id);
    
    List<Fruit> findAll();
    
    List<Fruit> findByCategory(Category category);
    
    List<Fruit> findByFruitNameContaining(String keyword);
    
    List<Fruit> findByOrigin(String origin);
    
    List<Fruit> findBySeason(String season);
    
    List<Fruit> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Fruit> findByStockQuantityGreaterThan(int minStock);
    
    boolean existsById(Long id);
    
    void delete(Fruit fruit);
    
    long count();
}