package com.fruitmall.domain.fruit.application.mapper;

import com.fruitmall.domain.fruit.application.dto.CategoryDto;
import com.fruitmall.domain.fruit.application.dto.CategoryRequestDto;
import com.fruitmall.domain.fruit.domain.Category;

public interface CategoryMapper {
    
    Category toEntity(CategoryRequestDto dto);
    
    CategoryDto toDto(Category category);
    
    void updateEntityFromDto(CategoryRequestDto dto, Category category);
}