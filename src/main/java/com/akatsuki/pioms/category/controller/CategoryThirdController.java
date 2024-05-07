package com.akatsuki.pioms.category.controller;

import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.category.service.CategoryThirdService;
import com.akatsuki.pioms.category.vo.ResponseCategoryPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category/third")
@Tag(name = "카테고리(소) 조회 컨트롤러", description = "카테고리(소) 조회 및 추가 수정")
public class CategoryThirdController {

    private final CategoryThirdService categoryThirdService;

    @Autowired
    public CategoryThirdController(CategoryThirdService categoryThirdService) {
        this.categoryThirdService = categoryThirdService;
    }

    @GetMapping("/all")
    @Operation(summary = "카테고리(소) 전체 조회", description = "단순 카테고리(소) 조회 기능")
    public ResponseEntity<List<CategoryThird>> getAllCategoryThird() {
        return ResponseEntity.ok().body(categoryThirdService.getAllCategoryThird());
    }

    @GetMapping("/all/{categorySecondCode}")
    @Operation(summary = "한 카테고리(중) code에 속한 카테고리(소) 조회", description = "카테고리(중) 속 카테고리(소) 조회")
    public ResponseEntity<List<CategoryThird>> getAllCategoryThirdofSecond() {
        return ResponseEntity.ok().body(categoryThirdService.getAllCategoryThirdofSecond());
    }

//    @PostMapping("/{categorySecondCode}")
//    @Operation(summary = "한 카테고리(중) code에 속한 카테고리(소) 등록", description = "카테고리(중) 속 카테고리(소) 등록")
//    public ResponseEntity<ResponseCategoryPost> postCategory(@RequestBody CategoryThird categoryThird) {
//        ResponseCategoryPost response = categoryThirdService.postCategoryThird(categoryThird);
//        return ResponseEntity.ok().body(response);
//    }

}
