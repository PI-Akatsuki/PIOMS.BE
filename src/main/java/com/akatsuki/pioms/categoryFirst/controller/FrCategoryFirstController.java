package com.akatsuki.pioms.categoryFirst.controller;

import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import com.akatsuki.pioms.categoryFirst.service.CategoryFirstService;
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
@RequestMapping("franchise/category/first")
@Tag(name = "[점주]카테고리(대) API", description = "카테고리(대) 조회")
public class FrCategoryFirstController {

    private final CategoryFirstService categoryFirstService;

    @Autowired
    public FrCategoryFirstController(CategoryFirstService categoryFirstService) {
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
}
