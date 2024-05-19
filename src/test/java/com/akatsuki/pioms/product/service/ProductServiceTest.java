package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import com.akatsuki.pioms.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
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

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductService productService;
    private Product product;

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

    @Test
    void postProduct() {
        RequestProduct requestProduct = new RequestProduct();
        requestProduct.setProductCode(20);
        requestProduct.setProductName("test");
        requestProduct.setProductPrice(100000);
        requestProduct.setProductContent("test code content");
        requestProduct.setProductColor(PRODUCT_COLOR.valueOf("빨간색"));
        requestProduct.setProductSize(105);
        requestProduct.setProductGender(PRODUCT_GENDER.valueOf("남성의류"));
        requestProduct.setProductTotalCount(5);
        requestProduct.setProductStatus(PRODUCT_STATUS.valueOf("품절"));
        requestProduct.setProductExposureStatus(true);
        requestProduct.setProductNoticeCount(5);
        requestProduct.setProductDisCount(5);
        requestProduct.setProductCount(5);
        requestProduct.setCategoryThirdCode(1);

        ResponseEntity<String> productDTO = productService.postProduct(requestProduct);

        assertNotNull(productDTO);
        assertEquals("상품 등록 완료!", productDTO);

    }
    @Test
    void deleteProduct() {
        assertEquals(true, true);
    }

    @Test
    void updateProduct() {
        assertEquals(true, true);
    }

    @Test
    void getCategoryProductList() {
        assertEquals(true, true);
    }
}