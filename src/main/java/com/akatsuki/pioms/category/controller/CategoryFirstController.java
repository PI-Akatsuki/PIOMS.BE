package com.akatsuki.pioms.category.controller;


import com.akatsuki.pioms.category.entity.CategoryFirst;
import com.akatsuki.pioms.category.service.CategoryFirstService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category/first")
@Tag(name = "카테고리(대) 조회 컨트롤러", description = "카테고리(대) 조회")
public class CategoryFirstController {

    private final CategoryFirstService categoryFirstService;

    @Autowired
    public CategoryFirstController(CategoryFirstService categoryFirstService) {
        this.categoryFirstService = categoryFirstService;
    }

    @GetMapping("")
    @Operation(summary = "카테고리(대) 전체 조회", description = "단순 카테고리(대) 조회 기능")
    public ResponseEntity<List<CategoryFirst>> getAllCategoryFirst() {
        return ResponseEntity.ok().body(categoryFirstService.getAllCategoryFirst());
    }

    @GetMapping("/{categoryFirstCode}")
    @Operation(summary = "카테고리(대) code로 카테고리(대) 하나 조회", description = "카테고리(대)코드로 카테고리(대) 하나 조회")
    public ResponseEntity<Optional<CategoryFirst>> getCategoryFirstByCode(@PathVariable int categoryFirstCode) {
        Optional<CategoryFirst> categoryFirst = categoryFirstService.findCategoryFirstByCode(categoryFirstCode);
        return ResponseEntity.ok().body(categoryFirst);
    }
}