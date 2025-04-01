package com.fruitmall.interfaces.web.fruit;

import com.fruitmall.domain.fruit.application.FruitService;
import com.fruitmall.domain.fruit.application.dto.FruitDto;
import com.fruitmall.domain.fruit.application.dto.FruitRegisterDto;
import com.fruitmall.domain.fruit.application.dto.FruitUpdateDto;
import com.fruitmall.domain.fruit.application.dto.StockUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/fruits")
@RequiredArgsConstructor
@Tag(name = "과일 상품 관리", description = "과일 상품 관련 API")
public class FruitController {

    private final FruitService fruitService;

    @Operation(summary = "과일 상품 등록", description = "새로운 과일 상품을 등록합니다 (관리자 전용)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FruitDto> register(@Valid @RequestBody FruitRegisterDto dto) {
        FruitDto fruitDto = fruitService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(fruitDto);
    }

    @Operation(summary = "과일 상품 조회", description = "ID로 과일 상품을 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<FruitDto> getFruit(
            @Parameter(description = "과일 상품 ID", required = true)
            @PathVariable Long id) {
        FruitDto fruitDto = fruitService.findById(id);
        return ResponseEntity.ok(fruitDto);
    }

    @Operation(summary = "전체 과일 상품 조회", description = "모든 과일 상품을 조회합니다")
    @GetMapping
    public ResponseEntity<List<FruitDto>> getAllFruits() {
        List<FruitDto> fruits = fruitService.findAll();
        return ResponseEntity.ok(fruits);
    }

    @Operation(summary = "카테고리별 과일 상품 조회", description = "카테고리 ID로 과일 상품을 조회합니다")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<FruitDto>> getFruitsByCategory(
            @Parameter(description = "카테고리 ID", required = true)
            @PathVariable Long categoryId) {
        List<FruitDto> fruits = fruitService.findByCategory(categoryId);
        return ResponseEntity.ok(fruits);
    }

    @Operation(summary = "과일 상품 검색", description = "키워드로 과일 상품을 검색합니다")
    @GetMapping("/search")
    public ResponseEntity<List<FruitDto>> searchFruits(
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword) {
        List<FruitDto> fruits = fruitService.searchByKeyword(keyword);
        return ResponseEntity.ok(fruits);
    }

    @Operation(summary = "원산지별 과일 상품 조회", description = "원산지로 과일 상품을 조회합니다")
    @GetMapping("/origin/{origin}")
    public ResponseEntity<List<FruitDto>> getFruitsByOrigin(
            @Parameter(description = "원산지", required = true)
            @PathVariable String origin) {
        List<FruitDto> fruits = fruitService.findByOrigin(origin);
        return ResponseEntity.ok(fruits);
    }

    @Operation(summary = "계절별 과일 상품 조회", description = "계절로 과일 상품을 조회합니다")
    @GetMapping("/season/{season}")
    public ResponseEntity<List<FruitDto>> getFruitsBySeason(
            @Parameter(description = "계절", required = true)
            @PathVariable String season) {
        List<FruitDto> fruits = fruitService.findBySeason(season);
        return ResponseEntity.ok(fruits);
    }

    @Operation(summary = "가격 범위로 과일 상품 조회", description = "최소/최대 가격 범위로 과일 상품을 조회합니다")
    @GetMapping("/price-range")
    public ResponseEntity<List<FruitDto>> getFruitsByPriceRange(
            @Parameter(description = "최소 가격", required = true)
            @RequestParam BigDecimal minPrice,
            @Parameter(description = "최대 가격", required = true)
            @RequestParam BigDecimal maxPrice) {
        List<FruitDto> fruits = fruitService.findByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(fruits);
    }

    @Operation(summary = "재고가 있는 과일 상품 조회", description = "재고가 있는 과일 상품만 조회합니다")
    @GetMapping("/in-stock")
    public ResponseEntity<List<FruitDto>> getInStockFruits() {
        List<FruitDto> fruits = fruitService.findInStock();
        return ResponseEntity.ok(fruits);
    }

    @Operation(summary = "과일 상품 정보 수정", description = "과일 상품 정보를 수정합니다 (관리자 전용)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FruitDto> updateFruit(
            @Parameter(description = "과일 상품 ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody FruitUpdateDto dto) {
        FruitDto updatedFruit = fruitService.update(id, dto);
        return ResponseEntity.ok(updatedFruit);
    }

    @Operation(summary = "과일 상품 재고 수정", description = "과일 상품의 재고를 수정합니다 (관리자 전용)")
    @PutMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FruitDto> updateStock(
            @Parameter(description = "과일 상품 ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateDto dto) {
        FruitDto updatedFruit = fruitService.updateStock(id, dto);
        return ResponseEntity.ok(updatedFruit);
    }

    @Operation(summary = "과일 상품 삭제", description = "과일 상품을 삭제합니다 (관리자 전용)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFruit(
            @Parameter(description = "과일 상품 ID", required = true)
            @PathVariable Long id) {
        fruitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}