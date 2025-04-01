package com.fruitmall.domain.fruit.application.mapper;

import com.fruitmall.domain.fruit.application.dto.FruitDto;
import com.fruitmall.domain.fruit.application.dto.FruitRegisterDto;
import com.fruitmall.domain.fruit.application.dto.FruitUpdateDto;
import com.fruitmall.domain.fruit.domain.Category;
import com.fruitmall.domain.fruit.domain.Fruit;
import org.springframework.stereotype.Component;

@Component
public class FruitMapperImpl implements FruitMapper {

    @Override
    public Fruit toEntity(FruitRegisterDto dto, Category category) {
        if (dto == null) {
            return null;
        }
        
        return Fruit.builder()
                .fruitName(dto.getFruitName())
                .origin(dto.getOrigin())
                .stockQuantity(dto.getStockQuantity())
                .price(dto.getPrice())
                .category(category)
                .season(dto.getSeason())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .build();
    }

    @Override
    public FruitDto toDto(Fruit fruit) {
        if (fruit == null) {
            return null;
        }
        
        return FruitDto.builder()
                .id(fruit.getId())
                .fruitName(fruit.getFruitName())
                .origin(fruit.getOrigin())
                .stockQuantity(fruit.getStockQuantity())
                .price(fruit.getPrice())
                .categoryId(fruit.getCategory() != null ? fruit.getCategory().getId() : null)
                .categoryName(fruit.getCategory() != null ? fruit.getCategory().getName() : null)
                .season(fruit.getSeason())
                .description(fruit.getDescription())
                .imageUrl(fruit.getImageUrl())
                .createdAt(fruit.getCreatedAt())
                .updatedAt(fruit.getUpdatedAt())
                .build();
    }

    @Override
    public void updateEntityFromDto(FruitUpdateDto dto, Fruit fruit, Category category) {
        if (dto == null) {
            return;
        }
        
        fruit.updateInfo(
            dto.getFruitName(),
            dto.getOrigin(),
            dto.getPrice(),
            category,
            dto.getSeason(),
            dto.getDescription(),
            dto.getImageUrl()
        );
    }
}