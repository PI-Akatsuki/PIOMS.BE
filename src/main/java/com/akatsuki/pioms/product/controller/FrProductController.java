package com.akatsuki.pioms.product.controller;

import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.service.ProductService;
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
@RequestMapping("franchise/product")
@Tag(name="가맹점 상품 CRUD 컨트롤러", description = "상품 조회,등록,수정,삭제")
public class FrProductController {

    private final ProductService productService;

    @Autowired
    public FrProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    @Operation(summary = "노출된 상품만 전체 조회" , description = "productExposureStatus=true 인 것만 조회")
    public ResponseEntity<List<ProductDTO>> getAllExposureProduct() {
        return ResponseEntity.ok().body(productService.getAllExposureProduct());
    }

    @GetMapping("/{productCode}")
    @Operation(summary = "상품코드로 상품 조회", description = "상품 코드로 상품 하나 단순 조회")
    public ResponseEntity<List<ResponseProduct>> getProductByCode(@PathVariable int productCode) {
        List<ProductDTO> productDTOS = productService.findProductByCode(productCode);
        List<ResponseProduct> responseProduct = new ArrayList<>();
        productDTOS.forEach(productDTO -> {
            responseProduct.add(new ResponseProduct(productDTO));
        });
        return ResponseEntity.ok(responseProduct);
    }

}
