package com.fruitmall.domain.fruit.application;

import com.fruitmall.domain.fruit.application.dto.FruitDto;
import com.fruitmall.domain.fruit.application.dto.FruitRegisterDto;
import com.fruitmall.domain.fruit.application.dto.FruitUpdateDto;
import com.fruitmall.domain.fruit.application.dto.StockUpdateDto;
import com.fruitmall.domain.fruit.application.mapper.FruitMapper;
import com.fruitmall.domain.fruit.domain.Category;
import com.fruitmall.domain.fruit.domain.CategoryRepository;
import com.fruitmall.domain.fruit.domain.Fruit;
import com.fruitmall.domain.fruit.domain.FruitRepository;
import com.fruitmall.global.error.BusinessException;
import com.fruitmall.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FruitService {

    private final FruitRepository fruitRepository;
    private final CategoryRepository categoryRepository;
    private final FruitMapper fruitMapper;

    // 과일 상품 등록
    @Transactional
    public FruitDto register(FruitRegisterDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "카테고리를 찾을 수 없습니다."));
        
        Fruit fruit = fruitMapper.toEntity(dto, category);
        Fruit savedFruit = fruitRepository.save(fruit);
        
        return fruitMapper.toDto(savedFruit);
    }

    // 과일 상품 조회
    public FruitDto findById(Long id) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
                
        return fruitMapper.toDto(fruit);
    }

    // 전체 과일 상품 조회
    public List<FruitDto> findAll() {
        return fruitRepository.findAll().stream()
                .map(fruitMapper::toDto)
                .collect(Collectors.toList());
    }

    // 카테고리별 과일 상품 조회
    public List<FruitDto> findByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "카테고리를 찾을 수 없습니다."));
                
        return fruitRepository.findByCategory(category).stream()
                .map(fruitMapper::toDto)
                .collect(Collectors.toList());
    }

    // 키워드로 과일 상품 검색
    public List<FruitDto> searchByKeyword(String keyword) {
        return fruitRepository.findByFruitNameContaining(keyword).stream()
                .map(fruitMapper::toDto)
                .collect(Collectors.toList());
    }

    // 원산지별 과일 상품 조회
    public List<FruitDto> findByOrigin(String origin) {
        return fruitRepository.findByOrigin(origin).stream()
                .map(fruitMapper::toDto)
                .collect(Collectors.toList());
    }

    // 계절별 과일 상품 조회
    public List<FruitDto> findBySeason(String season) {
        return fruitRepository.findBySeason(season).stream()
                .map(fruitMapper::toDto)
                .collect(Collectors.toList());
    }

    // 가격 범위로 과일 상품 조회
    public List<FruitDto> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return fruitRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(fruitMapper::toDto)
                .collect(Collectors.toList());
    }

    // 재고가 있는 과일 상품만 조회
    public List<FruitDto> findInStock() {
        return fruitRepository.findByStockQuantityGreaterThan(0).stream()
                .map(fruitMapper::toDto)
                .collect(Collectors.toList());
    }

    // 과일 상품 정보 업데이트
    @Transactional
    public FruitDto update(Long id, FruitUpdateDto dto) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
                
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND, "카테고리를 찾을 수 없습니다."));
                
        fruitMapper.updateEntityFromDto(dto, fruit, category);
        
        return fruitMapper.toDto(fruit);
    }

    // 재고 업데이트
    @Transactional
    public FruitDto updateStock(Long id, StockUpdateDto dto) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
                
        fruit.updateStock(dto.getStockQuantity());
        
        return fruitMapper.toDto(fruit);
    }

    // 재고 감소 (주문 시 사용)
    @Transactional
    public void decreaseStock(Long id, int quantity) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
                
        try {
            fruit.decreaseStock(quantity);
        } catch (IllegalStateException e) {
            throw new BusinessException(ErrorCode.OUT_OF_STOCK);
        }
    }

    // 재고 증가 (주문 취소 시 사용)
    @Transactional
    public void increaseStock(Long id, int quantity) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
                
        fruit.increaseStock(quantity);
    }

    // 과일 상품 삭제
    @Transactional
    public void delete(Long id) {
        Fruit fruit = fruitRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.FRUIT_NOT_FOUND));
                
        fruitRepository.delete(fruit);
    }
}