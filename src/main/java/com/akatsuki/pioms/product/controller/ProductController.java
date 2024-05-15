package com.akatsuki.pioms.product.controller;

import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.ResponseProducts;
import com.akatsuki.pioms.product.service.ProductService;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{productCode}")
    @Operation(summary = "상품코드로 상품 조회", description = "상품 코드로 상품 하나 단순 조회")
    public ResponseEntity<Product> getProductByCode(@PathVariable int productCode) {
        return ResponseEntity.ok().body(productService.findProductByCode(productCode));
    }

    @PostMapping("/create")
    @Operation(summary = "상품 등록")
    public ResponseEntity<String> postProduct(@RequestBody RequestProduct request, int requesterAdminCode) {
        return productService.postProduct(request, requesterAdminCode);
    }

    @DeleteMapping("/delete/{productCode}")
    @Operation(summary = "상품 삭제", description = "상품 코드로 상품 삭제")
    public String deleteProduct(@PathVariable int productCode/*, int requesterAdminCode*/) {

        return productService.deleteProduct(productCode/*, int requesterAdminCode*/);
    }

    @PostMapping("/update/{productCode}")
    @Operation(summary = "상품 정보 수정", description = "상품 수정 기능")
    public ResponseEntity<ResponseProduct> updateProduct(@PathVariable int productCode, @RequestBody RequestProduct request/*, int requesterAdminCode*/) {
        ResponseProduct response = productService.updateProduct(productCode, request/*, int requesterAdminCode*/);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/category/{categoryThirdCode}")
    public ResponseEntity<List<ResponseProducts>> getCategoryProductList(@PathVariable int categoryThirdCode) {
        return ResponseEntity.ok(productService.getCategoryProductList(categoryThirdCode));
    }
}
