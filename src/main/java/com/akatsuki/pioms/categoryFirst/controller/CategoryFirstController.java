package com.akatsuki.pioms.categoryFirst.controller;


import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import com.akatsuki.pioms.categoryFirst.service.CategoryFirstService;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirst;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("admin/category/first")
@Tag(name = "[관리자]카테고리(대) API", description = "카테고리(대) 조회")
public class CategoryFirstController {

    private final CategoryFirstService categoryFirstService;

    @Autowired
    public CategoryFirstController(CategoryFirstService categoryFirstService) {
        this.categoryFirstService = categoryFirstService;
    }

    @GetMapping("")
    @Operation(summary = "카테고리(대) 전체 조회", description = "단순 카테고리(대) 조회 기능")
    public ResponseEntity<List<ResponseCategoryFirst>> getAllCategoryFirst() {
        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.getAllCategoryFirst();
        List<ResponseCategoryFirst> responseCategory = new ArrayList<>();
        categoryFirstDTOS.forEach(categoryFirstDTO -> {
            responseCategory.add(new ResponseCategoryFirst(categoryFirstDTO));
        });
        return ResponseEntity.ok(responseCategory);
    }

    @GetMapping("/list/detail/{categoryFirstCode}")
    @Operation(summary = "카테고리(대) 상세 조회")
    public ResponseEntity<List<ResponseCategoryFirst>> getCategoryFirstByCode(@PathVariable int categoryFirstCode) {
        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.findCategoryFirstByCode(categoryFirstCode);
        List<ResponseCategoryFirst> responseCategory = new ArrayList<>();
        categoryFirstDTOS.forEach(categoryFirstDTO -> {
            responseCategory.add(new ResponseCategoryFirst(categoryFirstDTO));
        });
        return ResponseEntity.ok(responseCategory);
    }

    @PostMapping("/post")
    @Operation(summary = "카테고리(대) 등록", description = "단순 카테고리(대) 등록 기능")
    public ResponseEntity<String> postCategoryFirst(@RequestBody RequestCategoryFirst request) {
        return categoryFirstService.postCategoryFirst(request);

    }

    @PutMapping("/update/{categoryFirstCode}")
    @Operation(summary = "카테고리(대) 수정" ,description = "updateDate 자동 기입")
    public ResponseEntity<String> updateCategoryFirst(@PathVariable int categoryFirstCode, @RequestBody RequestCategoryFirst request) {
        return categoryFirstService.updateCategoryFirst(categoryFirstCode, request);
    }

    @DeleteMapping("/delete/{categoryFirstCode}")
    @Operation(summary = "카테고리(대) 삭제" ,description = "updateDate 자동 기입")
    public ResponseEntity<String> deleteCategoryFirst(@PathVariable int categoryFirstCode) {
        return categoryFirstService.deleteCategoryFirst(categoryFirstCode);
    }
}
