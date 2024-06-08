package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogServiceImpl;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import com.akatsuki.pioms.product.dto.ProductCreateDTO;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import com.akatsuki.pioms.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;
    @MockBean
    private LogServiceImpl logService;

    private Product product;

    @BeforeEach
    void init() {
        product = new Product();
        productRepository.save(product);
    }

    @Test void getAllProduct() {
        List <Product> productList = productRepository.findAll();
        List <ProductDTO> productDTOS = productService.getAllProduct();
        assertEquals(productList.size(), productDTOS.size());
        System.out.println();
    }
    @Test void getProductById() {
        int productCode = 1;
        List <Product> productList = productRepository.findByProductCode(productCode);
        List <ProductDTO> productDTOS = productService.findProductByCode(productCode);
        assertEquals(productList.size(), productDTOS.size());
    }

    @Test
    void getProductByCategoryThirdCode() {
        int categoryThirdCode = 1;
        List<Product> productList = productRepository.findAllByCategoryThirdCategoryThirdCode(categoryThirdCode);
        List<ResponseProduct> productDTOS = productService.getCategoryProductList(categoryThirdCode);
        System.out.println("productList = " + productList);
        assertEquals(productList.size(),productDTOS.size());
    }

    @Test
    @DisplayName("상품 신규 등록")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void createProduct() {
        // Given
        ProductCreateDTO productCreateDTO = new ProductCreateDTO();
        productCreateDTO.setProductName("Test Name");
        productCreateDTO.setProductPrice(100000);
        productCreateDTO.setProductContent("test content");
        productCreateDTO.setProductExposureStatus(true);
        productCreateDTO.setProductDiscount(12);
        productCreateDTO.setProductColor(PRODUCT_COLOR.valueOf("빨간색"));
        productCreateDTO.setProductSize(100);
        productCreateDTO.setProductTotalCount(1000);
        productCreateDTO.setProductStatus(PRODUCT_STATUS.valueOf("품절"));
        productCreateDTO.setProductNoticeCount(100);
        productCreateDTO.setProductCount(500);

        // When
        ProductDTO result = productService.createProduct(productCreateDTO);
        logService.saveLog("root", LogStatus.등록, product.getProductName(), "Product");

        assertNotNull(result);
        assertEquals("Test Name", result.getProductName());
    }



}
//    @Test
//    @WithMockUser(username = "root", roles = {"ROOT"})
//    void 상품_신규_등록() {
//        RequestProduct requestProduct = new RequestProduct();
//        requestProduct.setProductCode(20);
//        requestProduct.setProductName("test");
//        requestProduct.setProductPrice(100000);
//        requestProduct.setProductContent("test code content");
//        requestProduct.setProductColor(PRODUCT_COLOR.valueOf("빨간색"));
//        requestProduct.setProductSize(105);
//        requestProduct.setProductGender(PRODUCT_GENDER.valueOf("남성의류"));
//        requestProduct.setProductTotalCount(5);
//        requestProduct.setProductStatus(PRODUCT_STATUS.valueOf("품절"));
//        requestProduct.setProductExposureStatus(true);
//        requestProduct.setProductNoticeCount(5);
//        requestProduct.setProductDisCount(5);
//        requestProduct.setProductCount(5);
//        requestProduct.setCategoryThirdCode(1);
//
//        ResponseEntity<String> productDTO = productService.postProduct(requestProduct);
//        System.out.println("requestProduct = " + requestProduct);
//
//        assertNotNull(productDTO);
//        assertEquals("상품 등록 완료!", productDTO.getBody());
//
//    }
//
//    @Test
//    @WithMockUser(username = "root", roles = {"ROOT"})
//    @DisplayName("상품 수정")
//    void updateProduct() {
//        RequestProduct requestProduct = new RequestProduct();
//        requestProduct.setProductCode(1);
//        requestProduct.setProductName("testxx");
//        requestProduct.setProductPrice(100000000);
//        requestProduct.setProductContent("tttttttt");
//        requestProduct.setProductColor(PRODUCT_COLOR.valueOf("노란색"));
//        requestProduct.setProductSize(144);
//        requestProduct.setProductGender(PRODUCT_GENDER.valueOf("여성의류"));
//        requestProduct.setProductTotalCount(12123);
//        requestProduct.setProductStatus(PRODUCT_STATUS.valueOf("공급가능"));
//        requestProduct.setProductExposureStatus(true);
//        requestProduct.setProductNoticeCount(222);
//        requestProduct.setProductDisCount(1512);
//        requestProduct.setProductCount(214);
//        requestProduct.setCategoryThirdCode(1);
//
//        ResponseEntity<String> updateProduct = productService.updateProduct(1,requestProduct);
//        System.out.println("updateProduct = " + updateProduct);
//
////        logService.saveLog("root", LogStatus.수정, updatedProduct.getProductName(), "Product");
//        assertNotNull(updateProduct);
//        assertEquals("상품 수정 완료!",updateProduct.getBody());
//    }
//
//
//    @WithMockUser(username = "root", roles = {"ROOT"})
//    @Test
//    void deleteProduct() {
//        RequestProduct requestProduct = new RequestProduct();
//        requestProduct.setProductCode(200);
//        requestProduct.setProductName("test");
//        requestProduct.setProductPrice(41142124);
//        requestProduct.setProductContent("asdmogse");
//        requestProduct.setProductColor(PRODUCT_COLOR.valueOf("노란색"));
//        requestProduct.setProductSize(124);
//        requestProduct.setProductGender(PRODUCT_GENDER.valueOf("남성의류"));
//        requestProduct.setProductTotalCount(124);
//        requestProduct.setProductStatus(PRODUCT_STATUS.valueOf("공급가능"));
//        requestProduct.setProductExposureStatus(true);
//        requestProduct.setProductNoticeCount(312);
//        requestProduct.setProductDisCount(123);
//        requestProduct.setProductCount(412);
//        requestProduct.setCategoryThirdCode(100);
//
//        ResponseEntity<String> postProduct = productService.postProduct(requestProduct);
//
//        List<ResponseProduct> productList = productService.getCategoryProductList(requestProduct.getCategoryThirdCode());
//        boolean productExists = productList.stream()
//                .anyMatch(product -> product.getProductCode() == requestProduct.getProductCode());
//        assertFalse(productExists, "삭 성");
//    }
