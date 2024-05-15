package com.akatsuki.pioms.categoryThird.controller;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThirdUpdate;
import com.akatsuki.pioms.categoryThird.service.CategoryThirdService;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThirdPost;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThirdPost;
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
    public  ResponseEntity<CategoryThird> getCategoryThirdByCode(@PathVariable int categoryThirdCode) {
        return ResponseEntity.ok().body(categoryThirdService.findCategoryThirdByCode(categoryThirdCode));
    }
    @PostMapping("/create")
    public ResponseEntity<String> postCategoryThird(@RequestBody RequestCategoryThirdPost request, int requesterAdminCode) {
        return categoryThirdService.postCategory(request, requesterAdminCode);
    }

    @PostMapping("/update/{categoryThirdCode}")
    public ResponseEntity<String> updateCategoryThird(@PathVariable int categoryThirdCode, @RequestBody RequestCategoryThirdUpdate request, int requesterAdminCode) {
        return categoryThirdService.updateCategory(categoryThirdCode, request, requesterAdminCode);
    }

    @DeleteMapping("/delete/{categoryThirdCode}")
    @Operation(summary = "카테고리(소) 카테고리 삭제", description = "포함되어 있는 상품이 0개인 카테고리(소) 카테고리 삭제 기능")
    public String deleteCategoryThird(@PathVariable int categoryThirdCode/*, int requesterAdminCode*/) {
        return categoryThirdService.deleteCategoryThird(categoryThirdCode/*, int requesterAdminCode*/);
    }

}
