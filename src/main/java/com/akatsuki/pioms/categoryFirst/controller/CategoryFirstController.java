package com.akatsuki.pioms.categoryFirst.controller;


import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import com.akatsuki.pioms.categoryFirst.service.CategoryFirstService;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirstPost;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirstUpdate;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirstPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<List<ResponseCategoryFirst>> getAllCategoryFirst() {
        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.getAllCategoryFirst();
        List<ResponseCategoryFirst> responseCategory = new ArrayList<>();
        categoryFirstDTOS.forEach(categoryFirstDTO -> {
            responseCategory.add(new ResponseCategoryFirst(categoryFirstDTO));
        });
        return ResponseEntity.ok(responseCategory);
    }

    @GetMapping("/{categoryFirstCode}")
    @Operation(summary = "카테고리(대) 하나 조회", description = "카테고리(대)코드로 카테고리(대) 하나 조회")
    public ResponseEntity<List<ResponseCategoryFirst>> getCategoryFirstByCode(@PathVariable int categoryFirstCode) {
        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.findCategoryFirstByCode(categoryFirstCode);
        List<ResponseCategoryFirst> responseCategory = new ArrayList<>();
        categoryFirstDTOS.forEach(categoryFirstDTO -> {
            responseCategory.add(new ResponseCategoryFirst(categoryFirstDTO));
        });
        return ResponseEntity.ok(responseCategory);
    }

    @PostMapping("/post")
    public ResponseEntity<String> postCategoryFirst(@RequestBody RequestCategoryFirstPost request, int requesterAdminCode) {
        return categoryFirstService.postCategoryFirst(request, requesterAdminCode);

    }

    @PostMapping("/update/{categoryFirstCode}")
    @Operation(summary = "카테고리(대) 수정" ,description = "updateDate 자동 기입")
    public ResponseEntity<String> updateCategoryFirst(@PathVariable int categoryFirstCode, @RequestBody RequestCategoryFirstUpdate request, int requesterAdminCode) {
        return categoryFirstService.updateCategoryFirst(categoryFirstCode, request, requesterAdminCode);
    }
}
