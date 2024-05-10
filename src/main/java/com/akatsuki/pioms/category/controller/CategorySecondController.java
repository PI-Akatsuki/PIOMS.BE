package com.akatsuki.pioms.category.controller;

import com.akatsuki.pioms.category.entity.CategorySecond;
import com.akatsuki.pioms.category.service.CategorySecondService;
import com.akatsuki.pioms.category.vo.RequestCategorySecondPost;
import com.akatsuki.pioms.category.vo.RequestCategorySecondUpdate;
import com.akatsuki.pioms.category.vo.ResponseCategorySecondPost;
import com.akatsuki.pioms.category.vo.ResponseCategorySecondUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<CategorySecond>> getAllCategorySecond() {
        return ResponseEntity.ok().body(categorySecondService.getAllCategorySecond());
    }

    @GetMapping("/{categorySecondCode}")
    @Operation(summary = "카테고리(중) code로 카테고리(중) 하나 조회", description = "카테고리(중)코드로 카테고리(중) 하나 조회")
    public ResponseEntity<Optional<CategorySecond>> getCategorySecondByCode(@PathVariable int categorySecondCode) {
        Optional<CategorySecond> categorySecond = categorySecondService.findCategorySecondByCode(categorySecondCode);
        return ResponseEntity.ok().body(categorySecond);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseCategorySecondPost> postCategorySecond(@RequestBody RequestCategorySecondPost request) {
        ResponseCategorySecondPost response = categorySecondService.postCategorySecond(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/update/{categorySecondCode}")
    public ResponseEntity<ResponseCategorySecondUpdate> updateCategorySecond(@PathVariable int categorySecondCode, @RequestBody RequestCategorySecondUpdate request) {
        ResponseCategorySecondUpdate response = categorySecondService.updateCategorySecond(categorySecondCode, request);
        return ResponseEntity.ok().body(response);
    }

}
