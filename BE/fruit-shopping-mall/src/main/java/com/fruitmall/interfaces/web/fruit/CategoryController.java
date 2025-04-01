package com.fruitmall.interfaces.web.fruit;

import com.fruitmall.domain.fruit.application.CategoryService;
import com.fruitmall.domain.fruit.application.dto.CategoryDto;
import com.fruitmall.domain.fruit.application.dto.CategoryRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "카테고리 관리", description = "카테고리 관련 API")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록합니다 (관리자 전용)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> register(@Valid @RequestBody CategoryRequestDto dto) {
        CategoryDto categoryDto = categoryService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto);
    }

    @Operation(summary = "카테고리 조회", description = "ID로 카테고리를 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(
            @Parameter(description = "카테고리 ID", required = true)
            @PathVariable Long id) {
        CategoryDto categoryDto = categoryService.findById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @Operation(summary = "카테고리명으로 조회", description = "카테고리명으로 카테고리를 조회합니다")
    @GetMapping("/by-name")
    public ResponseEntity<CategoryDto> getCategoryByName(
            @Parameter(description = "카테고리명", required = true)
            @RequestParam String name) {
        CategoryDto categoryDto = categoryService.findByName(name);
        return ResponseEntity.ok(categoryDto);
    }

    @Operation(summary = "전체 카테고리 조회", description = "모든 카테고리를 조회합니다")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 정보를 수정합니다 (관리자 전용)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(
            @Parameter(description = "카테고리 ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto dto) {
        CategoryDto updatedCategory = categoryService.update(id, dto);
        return ResponseEntity.ok(updatedCategory);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다 (관리자 전용)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "카테고리 ID", required = true)
            @PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}