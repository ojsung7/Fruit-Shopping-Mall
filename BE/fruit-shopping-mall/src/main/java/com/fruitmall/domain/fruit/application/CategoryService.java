package com.fruitmall.domain.fruit.application;

import com.fruitmall.domain.fruit.application.dto.CategoryDto;
import com.fruitmall.domain.fruit.application.dto.CategoryRequestDto;
import com.fruitmall.domain.fruit.application.mapper.CategoryMapper;
import com.fruitmall.domain.fruit.domain.Category;
import com.fruitmall.domain.fruit.domain.CategoryRepository;
import com.fruitmall.domain.fruit.domain.FruitRepository;
import com.fruitmall.global.error.BusinessException;
import com.fruitmall.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FruitRepository fruitRepository;
    private final CategoryMapper categoryMapper;

    // 카테고리 등록
    @Transactional
    public CategoryDto register(CategoryRequestDto dto) {
        // 카테고리명 중복 확인
        if (categoryRepository.existsByName(dto.getName())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 카테고리명입니다.");
        }
        
        Category category = categoryMapper.toEntity(dto);
        Category savedCategory = categoryRepository.save(category);
        
        return categoryMapper.toDto(savedCategory);
    }

    // 카테고리 조회
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "카테고리를 찾을 수 없습니다."));
                
        return categoryMapper.toDto(category);
    }

    // 카테고리명으로 조회
    public CategoryDto findByName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "카테고리를 찾을 수 없습니다."));
                
        return categoryMapper.toDto(category);
    }

    // 전체 카테고리 조회
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카테고리 업데이트
    @Transactional
    public CategoryDto update(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "카테고리를 찾을 수 없습니다."));
        
        // 다른 카테고리와 이름 중복 확인
        if (!category.getName().equals(dto.getName()) && categoryRepository.existsByName(dto.getName())) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 카테고리명입니다.");
        }
        
        categoryMapper.updateEntityFromDto(dto, category);
        
        return categoryMapper.toDto(category);
    }

    // 카테고리 삭제
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "카테고리를 찾을 수 없습니다."));
        
        // 해당 카테고리에 속한 상품이 있는지 확인
        if (!fruitRepository.findByCategory(category).isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "해당 카테고리에 속한 상품이 있어 삭제할 수 없습니다.");
        }
        
        categoryRepository.delete(category);
    }
}