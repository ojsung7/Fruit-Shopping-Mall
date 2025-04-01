package com.fruitmall.domain.fruit.infra.persistence;

import com.fruitmall.domain.fruit.domain.Category;
import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.fruit.domain.FruitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JpaFruitRepository extends JpaRepository<Fruit, Long>, FruitRepository {
    
    List<Fruit> findByCategory(Category category);
    
    List<Fruit> findByFruitNameContaining(String keyword);
    
    List<Fruit> findByOrigin(String origin);
    
    List<Fruit> findBySeason(String season);
    
    List<Fruit> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Fruit> findByStockQuantityGreaterThan(int minStock);
}