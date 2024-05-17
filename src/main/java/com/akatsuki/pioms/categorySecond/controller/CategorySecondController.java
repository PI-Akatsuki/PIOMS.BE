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
import java.util.Optional;

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
    public ResponseEntity<List<CategorySecondDTO>> getAllCategorySecond() {
        return ResponseEntity.ok().body(categorySecondService.getAllCategorySecond());
    }

    @GetMapping("/{categorySecondCode}")
    @Operation(summary = "카테고리(중) code로 카테고리(중) 하나 조회", description = "카테고리(중)코드로 카테고리(중) 하나 조회")
    public ResponseEntity<List<ResponseCategorySecondPost>> getCategorySecondByCode(@PathVariable int categorySecondCode) {
        List<CategorySecondDTO> categorySecondDTOS = categorySecondService.findCategorySecondByCode(categorySecondCode);
        List<ResponseCategorySecondPost> categorySecondPostList = new ArrayList<>();
        categorySecondDTOS.forEach(categorySecondDTO -> {
            categorySecondPostList.add(new ResponseCategorySecondPost(categorySecondDTO));
        });
        return ResponseEntity.ok(categorySecondPostList);
    }

    @PostMapping("/create")
    public ResponseEntity<String> postCategorySecond(@RequestBody RequestCategorySecondPost request, int requesterAdminCode) {
        return categorySecondService.postCategorySecond(request, requesterAdminCode);

    }

    @PostMapping("/update/{categorySecondCode}")
    public ResponseEntity<String> updateCategorySecond(@PathVariable int categorySecondCode, @RequestBody RequestCategorySecondUpdate request, int requesterAdminCode) {
        return categorySecondService.updateCategorySecond(categorySecondCode, request, requesterAdminCode);
    }

}
