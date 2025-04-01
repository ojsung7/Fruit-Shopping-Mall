package com.fruitmall.domain.fruit.application.mapper;

import com.fruitmall.domain.fruit.application.dto.FruitDto;
import com.fruitmall.domain.fruit.application.dto.FruitRegisterDto;
import com.fruitmall.domain.fruit.application.dto.FruitUpdateDto;
import com.fruitmall.domain.fruit.domain.Category;
import com.fruitmall.domain.fruit.domain.Fruit;

public interface FruitMapper {
    
    Fruit toEntity(FruitRegisterDto dto, Category category);
    
    FruitDto toDto(Fruit fruit);
    
    void updateEntityFromDto(FruitUpdateDto dto, Fruit fruit, Category category);
}