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
@RequestMapping("/category/second")
@Tag(name = "카테고리(중) 조회 컨트롤러", description = "카테고리(중) 조회")
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

    @GetMapping("/categoryfirst/{categoryFirstCode}")
    @Operation(summary = "카테고리(대)에 속한 카테고리(중) 목록 조회")
    public ResponseEntity<List<ResponseCategorySecond>> getCategorySecondInCategoryFirst(@PathVariable int categoryFirstCode) {
        return ResponseEntity.ok(categorySecondService.getCategorySecondInFirst(categoryFirstCode));
    }

    @PostMapping("/create")
    public ResponseEntity<String> postCategorySecond(@RequestBody RequestCategorySecond request, int requesterAdminCode) {
        return categorySecondService.postCategorySecond(request, requesterAdminCode);

    }

    @PutMapping("/update/{categorySecondCode}")
    public ResponseEntity<String> updateCategorySecond(@PathVariable int categorySecondCode, @RequestBody RequestCategorySecond request/*, int requesterAdminCode*/) {
        return categorySecondService.updateCategorySecond(categorySecondCode, request/*, requesterAdminCode*/);
    }

}
