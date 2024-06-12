package com.akatsuki.pioms.product.controller;

import com.akatsuki.pioms.product.aggregate.ResponseProductWithImage;
import com.akatsuki.pioms.product.dto.KakaoAlertRequest;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.service.ProductService;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("admin/product")
@Tag(name="[관리자]상품 API", description = "상품 조회,등록,수정,삭제")
public class AdminProductController {

    private final ProductService productService;

    @Autowired
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    @Operation(summary = "상품 전체 조회", description = "상품 전체 단순 조회 기능")
    public ResponseEntity<List<ProductDTO>> getAllProduct() {
        return ResponseEntity.ok().body(productService.getAllProduct());
    }

    @GetMapping("/list/detail/{productCode}")
    @Operation(summary = "상품 상세 조회", description = "상품 코드로 상품 하나 단순 조회")
    public ResponseEntity<List<ResponseProduct>> getProductByCode(@PathVariable int productCode) {
        List<ProductDTO> productDTOS = productService.findProductByCode(productCode);
        List<ResponseProduct> responseProduct = new ArrayList<>();
        productDTOS.forEach(productDTO -> {
            responseProduct.add(new ResponseProduct(productDTO));
        });
        return ResponseEntity.ok(responseProduct);
    }

    @PostMapping("/create")
    @Operation(summary = "상품 등록")
    public ResponseEntity<String> postProduct(@RequestBody RequestProduct request) {
        return productService.postProduct(request);
    }

    @DeleteMapping("/delete/{productCode}")
    @Operation(summary = "상품 삭제", description = "상품 코드로 상품 삭제")
    public ResponseEntity<String> deleteProduct(@PathVariable int productCode) {
        return productService.deleteProduct(productCode);
    }

    @PutMapping("/update/{productCode}")
    @Operation(summary = "상품 정보 수정", description = "상품 수정 기능")
    public ResponseEntity<String> updateProduct(@PathVariable int productCode, @RequestBody RequestProduct request) {
        return productService.updateProduct(productCode, request);
    }

    @GetMapping("/category/{categoryThirdCode}")
    @Operation(summary = "카테고리별 상품 조회", description = "카테고리 코드로 상품 목록 조회")
    public ResponseEntity<List<ResponseProduct>> getCategoryProductList(@PathVariable int categoryThirdCode) {
        return ResponseEntity.ok(productService.getCategoryProductList(categoryThirdCode));
    }

    @PostMapping("/image")
    @Operation(summary = "상품 이미지 등록", description = "상품 이미지 등록 기능")
    public ResponseEntity<Boolean> postImage(RequestProduct request) {
        Boolean result = Boolean.valueOf(productService.postProductWithImage(request, request.getFile()));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/productImage")
    @Operation(summary = "상품 이미지 조회", description = "상품 이미지 조회 기능")
    public ResponseEntity<List<ResponseProductWithImage>> getProducts() {
        List<ResponseProductWithImage> responseProductImage = productService.getAllProductWithImage();
        if (responseProductImage.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        return ResponseEntity.ok(responseProductImage);
    }

    @PostMapping("/sendKakaoAlert")
    @Operation(summary = "카카오톡 알림 전송")
    public ResponseEntity<String> sendKakaoAlert(@RequestBody KakaoAlertRequest request) {
        try {
            productService.sendKakaoAlert(request.getProductName(), request.getStockQuantity());
            return ResponseEntity.ok("카카오톡 알림 전송 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("카카오톡 알림 전송 실패");
        }
    }


    @PutMapping("/update/image/{productCode}")
    @Operation(summary = "상품 정보(+이미지) 수정", description = "상품(+이미지) 수정 기능")
    public ResponseEntity<String> updateProduct2(@PathVariable int productCode, RequestProduct request) {
        return productService.updateProductWithImage(productCode, request);
    }

}
