package com.akatsuki.pioms.category.controller;

import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.category.service.CategoryThirdService;
import com.akatsuki.pioms.category.vo.RequestCategoryPost;
import com.akatsuki.pioms.category.vo.RequestCategoryUpdate;
import com.akatsuki.pioms.category.vo.ResponseCategoryPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category/third")
@Tag(name = "카테고리(소) 조회 컨트롤러", description = "카테고리(소) 조회 및 추가 수정")
public class CategoryThirdController {

    private final CategoryThirdService categoryThirdService;

    @Autowired
    public CategoryThirdController(CategoryThirdService categoryThirdService) {
        this.categoryThirdService = categoryThirdService;
    }

    @GetMapping("")
    @Operation(summary = "카테고리(소) 전체 조회", description = "단순 카테고리(소) 조회 기능")
    public ResponseEntity<List<CategoryThird>> getAllCategoryThird() {
        return ResponseEntity.ok().body(categoryThirdService.getAllCategoryThird());
    }

    @GetMapping("/{categoryThirdCode}")
    @Operation(summary = "카테고리(소) code로 카테고리(소) 하나 조회", description = "카테고리(소)코드로 카테고리(소) 하나 조회")
    public  ResponseEntity<Optional<CategoryThird>> getCategoryThirdByCode(@PathVariable int categoryThirdCode) {
        Optional<CategoryThird> categoryThird = categoryThirdService.findCategoryThirdByCode(categoryThirdCode);
        return ResponseEntity.ok().body(categoryThird);
    }
    @PostMapping("/create")
    public ResponseEntity<ResponseCategoryPost> postCategoryThird(@RequestBody RequestCategoryPost request) {
        ResponseCategoryPost response = categoryThirdService.postCategory(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/update/{categoryThirdCode}")
    public ResponseEntity<ResponseCategoryPost> updateCategoryThird(@PathVariable int categoryThirdCode, @RequestBody RequestCategoryUpdate request) {
        ResponseCategoryPost response = categoryThirdService.updateCategory(categoryThirdCode, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{categoryThirdCode}")
    @Operation(summary = "카테고리(소) 카테고리 삭제", description = "포함되어 있는 상품이 0개인 카테고리(소) 카테고리 삭제 기능")
    public ResponseEntity<CategoryThird> deleteCategory(@PathVariable int categoryThirdCode) {
        CategoryThird categoryThird = categoryThirdService.deleteCategory(categoryThirdCode);
        return ResponseEntity.ok().body(categoryThird);
    }
}
