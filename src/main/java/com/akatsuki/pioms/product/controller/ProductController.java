package com.akatsuki.pioms.product.controller;

import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@Tag(name="상품 CRUD 컨트롤러", description = "상품 조회,등록,수정,삭제")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    @Operation(summary = "상품 전체 조회", description = "상품 전체 단순 조회 기능")
    public ResponseEntity<List<Product>> getAllProduct() {
        return ResponseEntity.ok().body(productService.getAllProduct());
    }

}
