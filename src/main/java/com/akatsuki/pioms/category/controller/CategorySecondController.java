package com.akatsuki.pioms.category.controller;

import com.akatsuki.pioms.category.entity.CategorySecond;
import com.akatsuki.pioms.category.service.CategorySecondService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category/second")
@Tag(name = "카테고리(중) 조회 컨트롤러", description = "카테고리(중) 조회")
public class CategorySecondController {

    private final CategorySecondService categorySecondService;

    @Autowired
    public CategorySecondController(CategorySecondService categorySecondService) {
        this.categorySecondService = categorySecondService;
    }
    @GetMapping("/all")
    @Operation(summary = "카테고리(중) 전체 조회", description = "단순 카테고리(중) 조회 기능")
    public ResponseEntity<List<CategorySecond>> getAllCategorySecond() {
        return ResponseEntity.ok().body(categorySecondService.getAllCategorySecond());
    }

    @GetMapping("/all/{categoryFirstCode}")
    @Operation(summary = "한 카테고리(대) code에 속한 카테고리(중) 조회", description = "카테고리(대) 속 카테고리(중) 조회")
    public ResponseEntity<List<CategorySecond>> getAllCategorySecondofFirst() {
        return ResponseEntity.ok().body(categorySecondService.getAllCategorySecondofFirst());
    }

}
