package com.akatsuki.pioms.categoryThird.controller;

import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThird;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import com.akatsuki.pioms.categoryThird.service.CategoryThirdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("admin/category/third")
@Tag(name = "[관리자]카테고리(소) 조회 컨트롤러", description = "카테고리(소) 조회 및 추가 수정")
public class CategoryThirdController {

    private final CategoryThirdService categoryThirdService;

    @Autowired
    public CategoryThirdController(CategoryThirdService categoryThirdService) {
        this.categoryThirdService = categoryThirdService;
    }

    @GetMapping("")
    @Operation(summary = "카테고리(소) 전체 조회", description = "단순 카테고리(소) 조회 기능")
    public ResponseEntity<List<CategoryThirdDTO>> getAllCategoryThird() {
        return ResponseEntity.ok().body(categoryThirdService.getAllCategoryThird());
    }

    @GetMapping("/{categoryThirdCode}")
    @Operation(summary = "카테고리(소) 상세 조회")
    public ResponseEntity<List<ResponseCategoryThird>> getCategoryThirdByCode(@PathVariable int categoryThirdCode) {
        List<CategoryThirdDTO> categoryThirdDTOS = categoryThirdService.findCategoryThirdByCode(categoryThirdCode);
        List<ResponseCategoryThird> responseCategory = new ArrayList<>();
        categoryThirdDTOS.forEach(categoryThirdDTO -> {
            responseCategory.add(new ResponseCategoryThird(categoryThirdDTO));
        });
        return ResponseEntity.ok(responseCategory);
    }

    @GetMapping("/list/detail/categorysecond/{categorySecondCode}")
    @Operation(summary = "카테고리(중)에 속한 카테고리(소) 목록 조회")
    public ResponseEntity<List<ResponseCategoryThird>> getCategoryThirdInCategorySecond(@PathVariable int categorySecondCode) {
        return ResponseEntity.ok(categoryThirdService.getCategoryThirdInSecond(categorySecondCode));
    }

    @PostMapping("/create")
    @Operation(summary = "카테고리(소) 생성")
    public ResponseEntity<String> postCategoryThird(@RequestBody RequestCategoryThird request) {
        return categoryThirdService.postCategory(request);
    }

    @PutMapping("/update/{categoryThirdCode}")
    @Operation(summary = "카테고리(소) 수정")
    public ResponseEntity<String> updateCategoryThird(@PathVariable int categoryThirdCode, @RequestBody RequestCategoryThird request) {
        return categoryThirdService.updateCategory(categoryThirdCode, request);
    }

    @DeleteMapping("/delete/{categoryThirdCode}")
    @Operation(summary = "카테고리(소) 카테고리 삭제", description = "포함되어 있는 상품이 0개인 카테고리(소) 카테고리 삭제 기능")
    public ResponseEntity<String> deleteCategoryThird(@PathVariable int categoryThirdCode) {
        return categoryThirdService.deleteCategoryThird(categoryThirdCode);
    }

}
