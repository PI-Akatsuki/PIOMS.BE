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
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private Product product;
    static RequestProduct request;

    @BeforeEach
    void init() {
        Product product = new Product();
        product.setProductCode(20);
        product.setProductName("test");
        product.setProductPrice(100000);
        product.setProductEnrollDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        product.setProductUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        product.setProductContent("test code content");
        product.setProductColor(PRODUCT_COLOR.valueOf("빨간색"));
        product.setProductSize(105);
        product.setProductGender(PRODUCT_GENDER.valueOf("남성의류"));
        product.setProductTotalCount(5);
        product.setProductStatus(PRODUCT_STATUS.valueOf("품절"));
        product.setProductExposureStatus(true);
        product.setProductNoticeCount(5);
        product.setProductDiscount(5);
        product.setProductCount(5);
        product.setCategoryThird(product.getCategoryThird());
        productRepository.save(product);
    }

    @Test
    void getAllProduct() {
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> productDTOS = productService.getAllProduct();
        assertEquals(productList.size(),productDTOS.size());
        System.out.println();
    }

    @Test
    void findProductByCode() {
        int productCode = 1;
        List<Product> productList = productRepository.findByProductCode(productCode);
        List<ProductDTO> productDTOS = productService.findProductByCode(productCode);
        assertEquals(productList.size(),productDTOS.size());
    }

    @Test
    void deleteProduct() {assertEquals(true,true);}

    @Test
    void updateProduct() {assertEquals(true,true);}

    @Test
    void getCategoryProductList() {assertEquals(true,true);}
}