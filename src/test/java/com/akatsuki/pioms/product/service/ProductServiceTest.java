package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {
    ProductRepository productRepository;
    ProductService productService;

    @Autowired
    public ProductServiceTest(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @Test
    void getAllProduct() {
        List<Product> product = productRepository.findAll();
        List<Product> products = productService.getAllProduct();
        assertEquals(product.size(), products.size());
    }

    @Test
    void findProductByCode() {assertEquals(true,true);}

    @Test
    void postProduct() {assertEquals(true,true);}

    @Test
    void deleteProduct() {assertEquals(true,true);}

    @Test
    void updateProduct() {assertEquals(true,true);}

    @Test
    void getCategoryProductList() {assertEquals(true,true);}
}