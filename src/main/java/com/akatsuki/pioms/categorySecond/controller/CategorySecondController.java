package com.akatsuki.pioms.categorySecond.controller;

import com.akatsuki.pioms.categorySecond.aggregate.*;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondDTO;
import com.akatsuki.pioms.categorySecond.service.CategorySecondService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("admin/category/second")
@Tag(name = "[관리자]카테고리(중) API", description = "카테고리(중) 조회")
public class CategorySecondController {

    private final CategorySecondService categorySecondService;

    @Autowired
    public CategorySecondController(CategorySecondService categorySecondService) {
        this.categorySecondService = categorySecondService;
    }
    @GetMapping("")
    @Operation(summary = "카테고리(중) 전체 조회", description = "단순 카테고리(중) 조회 기능")
    public ResponseEntity<List<ResponseCategorySecond>> getAllCategorySecond() {
        List<CategorySecondDTO> categorySecondDTOS = categorySecondService.getAllCategorySecond();
        List<ResponseCategorySecond> responseCategory = new ArrayList<>();
        categorySecondDTOS.forEach(categorySecondDTO -> {
            responseCategory.add(new ResponseCategorySecond(categorySecondDTO));
        });
        return ResponseEntity.ok(responseCategory);
    }

    @GetMapping("/{categorySecondCode}")
    @Operation(summary = "카테고리(중) 상세 조회")
    public ResponseEntity<List<ResponseCategorySecond>> getCategorySecondByCode(@PathVariable int categorySecondCode) {
        List<CategorySecondDTO> categorySecondDTOS = categorySecondService.findCategorySecondByCode(categorySecondCode);
        List<ResponseCategorySecond> responseCategory = new ArrayList<>();
        categorySecondDTOS.forEach(categorySecondDTO -> {
            responseCategory.add(new ResponseCategorySecond(categorySecondDTO));
        });
        return ResponseEntity.ok(responseCategory);
    }

    @GetMapping("/list/detail/categoryfirst/{categoryFirstCode}")
    @Operation(summary = "카테고리(대)에 속한 카테고리(중) 목록 조회")
    public ResponseEntity<List<ResponseCategorySecond>> getCategorySecondInCategoryFirst(@PathVariable int categoryFirstCode) {
        return ResponseEntity.ok(categorySecondService.getCategorySecondInFirst(categoryFirstCode));
    }

    @PostMapping("/create")
    @Operation(summary = "카테고리(중) 등록", description = "")
    public ResponseEntity<String> postCategorySecond(@RequestBody RequestCategorySecond request) {
        return categorySecondService.postCategorySecond(request);

    }

    @PutMapping("/update/{categorySecondCode}")
    @Operation(summary = "카태고리(중) 수정 ", description = "")
    public ResponseEntity<String> updateCategorySecond(@PathVariable int categorySecondCode, @RequestBody RequestCategorySecond request) {
        return categorySecondService.updateCategorySecond(categorySecondCode, request);
    }

    @DeleteMapping("/delete/{categorySecondCode}")
    @Operation(summary = "카태고리(중) 삭제 ", description = "")
    public ResponseEntity<String> deleteCategorySecond(@PathVariable int categorySecondCode) {
        return categorySecondService.deleteCategorySecond(categorySecondCode);
    }
}
