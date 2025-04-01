package com.fruitmall.domain.fruit.application.mapper;

import com.fruitmall.domain.fruit.application.dto.CategoryDto;
import com.fruitmall.domain.fruit.application.dto.CategoryRequestDto;
import com.fruitmall.domain.fruit.domain.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toEntity(CategoryRequestDto dto) {
        if (dto == null) {
            return null;
        }
        
        return Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    @Override
    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    @Override
    public void updateEntityFromDto(CategoryRequestDto dto, Category category) {
        if (dto == null) {
            return;
        }
        
        category.updateInfo(
            dto.getName(),
            dto.getDescription()
        );
    }
}