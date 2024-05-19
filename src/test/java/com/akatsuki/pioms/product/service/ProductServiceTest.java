package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import com.akatsuki.pioms.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductService productService;
    private Product product;
    static RequestProduct request;

    @Test void getAllProduct() {
        List <Product> productList = productRepository.findAll();
        List <ProductDTO> productDTOS = productService.getAllProduct();
        assertEquals(productList.size(), productDTOS.size());
        System.out.println();
    }
    @Test void findProductByCode() {
        int productCode = 1;
        List < Product > productList = productRepository.findByProductCode(productCode);
        List < ProductDTO > productDTOS = productService.findProductByCode(productCode);
        assertEquals(productList.size(), productDTOS.size());
    }
    @Test void deleteProduct() {
        assertEquals(true, true);
    }
    @Test void updateProduct() {
        assertEquals(true, true);
    }
    @Test void getCategoryProductList() {
        assertEquals(true, true);
    }
}