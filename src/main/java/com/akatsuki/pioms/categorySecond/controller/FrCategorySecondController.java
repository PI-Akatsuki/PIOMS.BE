package com.akatsuki.pioms.categorySecond.controller;

import com.akatsuki.pioms.categorySecond.aggregate.ResponseCategorySecond;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondDTO;
import com.akatsuki.pioms.categorySecond.service.CategorySecondService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("franchise/category/second")
@Tag(name = "[점주]카테고리(중) API")
public class FrCategorySecondController {
    private final CategorySecondService categorySecondService;

    public FrCategorySecondController(CategorySecondService categorySecondService) {
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
}
