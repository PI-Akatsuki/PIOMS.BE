package com.akatsuki.pioms.product.controller;

import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.service.ProductService;
import com.akatsuki.pioms.product.vo.RequestProductPost;
import com.akatsuki.pioms.product.vo.ResponseProductPost;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Optional<Product>> getProductByCode(@PathVariable int productCode) {
        Optional<Product> product = productService.findProductByCode(productCode);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping("/create")
    @Operation(summary = "상품 등록")
    public ResponseEntity<ResponseProductPost> postProduct(@RequestBody RequestProductPost request) {
        ResponseProductPost response = productService.postProduct(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete/{productCode}")
    @Operation(summary = "상품 삭제", description = "상품 코드로 상품 삭제")
    public ResponseEntity<Product> deleteProduct(@PathVariable int productCode) {
        Product product = productService.deleteProduct(productCode);
        return ResponseEntity.ok().body(product);
    }
}
