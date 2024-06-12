package com.akatsuki.pioms.categoryThird.controller;

import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThird;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import com.akatsuki.pioms.categoryThird.service.CategoryThirdService;
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
@RequestMapping("franchise/category/third")
@Tag(name = "[점주]카테고리(소) API")
public class FrCategoryThirdController {

    private final CategoryThirdService categoryThirdService;

    @Autowired
    public FrCategoryThirdController(CategoryThirdService categoryThirdService) {
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
}
