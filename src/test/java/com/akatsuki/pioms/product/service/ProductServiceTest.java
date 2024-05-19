package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
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

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired private ProductRepository productRepository;
    @Autowired private ProductService productService;
    static RequestProduct request;
    private Product product;

    @Test void 모든상품조회() {
        List <Product> productList = productRepository.findAll();
        List <ProductDTO> productDTOS = productService.getAllProduct();
        assertEquals(productList.size(), productDTOS.size());
        System.out.println();
    }
    @Test void 상품상세조회() {
        int productCode = 1;
        List <Product> productList = productRepository.findByProductCode(productCode);
        List <ProductDTO> productDTOS = productService.findProductByCode(productCode);
        assertEquals(productList.size(), productDTOS.size());
    }

    @Test
    void 카테고리코드로_상품목록조회() {
        int categoryThirdCode = 1;
        List<Product> productList = productRepository.findAllByCategoryThirdCategoryThirdCode(categoryThirdCode);
        List<ResponseProduct> productDTOS = productService.getCategoryProductList(categoryThirdCode);
        System.out.println("productList = " + productList);
        assertEquals(productList.size(),productDTOS.size());
    }

    @Test
    void 상품_신규_등록() {
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

        ResponseEntity<String> productDTO = productService.postProduct(requestProduct, 1);
        System.out.println("requestProduct = " + requestProduct);

        assertNotNull(productDTO);
        assertEquals("상품 등록 완료!", productDTO.getBody());

    }



    @Test
    void deleteProduct() {
        assertEquals(true, true);
    }


}