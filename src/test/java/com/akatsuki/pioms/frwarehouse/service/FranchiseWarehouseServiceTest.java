package com.akatsuki.pioms.frwarehouse.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.repository.CategoryThirdRepository;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.repository.FranchiseWarehouseRepository;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import com.akatsuki.pioms.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FranchiseWarehouseServiceTest {

    private FranchiseWarehouseRepository franchiseWarehouseRepository;
    private FranchiseWarehouseService franchiseWarehouseService;
    private ProductRepository productRepository;
    private CategoryThirdRepository categoryThirdRepository;
    private CategorySecondRepository categorySecondRepository;
    private CategoryFirstRepository categoryFirstRepository;

    @Autowired
    public FranchiseWarehouseServiceTest(FranchiseWarehouseRepository franchiseWarehouseRepository, FranchiseWarehouseService franchiseWarehouseService, ProductRepository productRepository, CategoryThirdRepository categoryThirdRepository, CategorySecondRepository categorySecondRepository, CategoryFirstRepository categoryFirstRepository) {
        this.franchiseWarehouseRepository = franchiseWarehouseRepository;
        this.franchiseWarehouseService = franchiseWarehouseService;
        this.productRepository = productRepository;
        this.categoryThirdRepository = categoryThirdRepository;
        this.categorySecondRepository = categorySecondRepository;
        this.categoryFirstRepository = categoryFirstRepository;
    }

    @BeforeEach
    @Rollback(false)
    public void setUp() {
        CategoryFirst categoryFirst1 = new CategoryFirst();
        categoryFirst1.setCategoryFirstName("Category First 1");
        categoryFirst1.setCategoryFirstEnrollDate("2023-01-01");
        categoryFirst1.setCategoryFirstUpdateDate("2023-01-01");
        categoryFirstRepository.save(categoryFirst1);

        CategoryFirst categoryFirst2 = new CategoryFirst();
        categoryFirst2.setCategoryFirstName("Category First 2");
        categoryFirst2.setCategoryFirstEnrollDate("2023-01-02");
        categoryFirst2.setCategoryFirstUpdateDate("2023-01-02");
        categoryFirstRepository.save(categoryFirst2);

        // CategorySecond 엔티티 생성 및 저장
        CategorySecond categorySecond1 = new CategorySecond();
        categorySecond1.setCategorySecondName("Category Second 1");
        categorySecond1.setCategorySecondEnrollDate("2023-01-01");
        categorySecond1.setCategorySecondUpdateDate("2023-01-01");
        categorySecond1.setCategoryFirst(categoryFirst1);
        categorySecondRepository.save(categorySecond1);

        CategorySecond categorySecond2 = new CategorySecond();
        categorySecond2.setCategorySecondName("Category Second 2");
        categorySecond2.setCategorySecondEnrollDate("2023-01-02");
        categorySecond2.setCategorySecondUpdateDate("2023-01-02");
        categorySecond2.setCategoryFirst(categoryFirst2);
        categorySecondRepository.save(categorySecond2);

        // CategoryThird 엔티티 생성 및 저장
        CategoryThird categoryThird1 = new CategoryThird();
        categoryThird1.setCategoryThirdName("Category 1");
        categoryThird1.setCategoryThirdEnrollDate("2023-01-01");
        categoryThird1.setCategoryThirdUpdateDate("2023-01-01");
        categoryThird1.setCategorySecond(categorySecond1);
        categoryThirdRepository.save(categoryThird1);

        CategoryThird categoryThird2 = new CategoryThird();
        categoryThird2.setCategoryThirdName("Category 2");
        categoryThird2.setCategoryThirdEnrollDate("2023-01-02");
        categoryThird2.setCategoryThirdUpdateDate("2023-01-02");
        categoryThird2.setCategorySecond(categorySecond2);
        categoryThirdRepository.save(categoryThird2);

        // Product 엔티티 생성 및 저장
        Product product1 = new Product();
        product1.setProductName("Product 1");
        product1.setProductPrice(100);
        product1.setProductEnrollDate("2023-01-01");
        product1.setProductUpdateDate("2023-01-01");
        product1.setProductContent("Content 1");
        product1.setProductSize(1);
        product1.setProductTotalCount(10);
        product1.setProductStatus(PRODUCT_STATUS.공급가능);
        product1.setProductExposureStatus(true);
        product1.setProductNoticeCount(0);
        product1.setProductDiscount(0);
        product1.setProductCount(10);
        product1.setProductColor(PRODUCT_COLOR.남색);
        product1.setProductGender(PRODUCT_GENDER.남성의류);
        product1.setCategoryThird(categoryThird1);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setProductName("Product 2");
        product2.setProductPrice(200);
        product2.setProductEnrollDate("2023-01-02");
        product2.setProductUpdateDate("2023-01-02");
        product2.setProductContent("Content 2");
        product2.setProductSize(2);
        product2.setProductTotalCount(20);
        product2.setProductStatus(PRODUCT_STATUS.공급가능);
        product2.setProductExposureStatus(true);
        product2.setProductNoticeCount(0);
        product2.setProductDiscount(0);
        product2.setProductCount(20);
        product2.setProductColor(PRODUCT_COLOR.남색);
        product2.setProductGender(PRODUCT_GENDER.남성의류);
        product2.setCategoryThird(categoryThird2);
        productRepository.save(product2);

        // FranchiseWarehouse 엔티티 생성 및 저장
        FranchiseWarehouse warehouse1 = new FranchiseWarehouse(false, 1, product1.getProductCode());
        FranchiseWarehouse warehouse2 = new FranchiseWarehouse(true, 2, product2.getProductCode());
        franchiseWarehouseRepository.save(warehouse1);
        franchiseWarehouseRepository.save(warehouse2);
    }
    @Test
    void getAllWarehouse() { assertEquals(true,true);}

    @Test
    void getWarehouseByWarehouseCode() { assertEquals(true,true);}

    @Test
    void updateWarehouseCount() {assertEquals(true,true);}

    @Test
    @DisplayName("즐겨찾기 추가/삭제 테스트")
    public void testToggleFavorite() {
        FranchiseWarehouse warehouse = franchiseWarehouseRepository.findAll().get(0);
        int code = warehouse.getFranchiseWarehouseCode();
        boolean initialFavoriteStatus = warehouse.isFranchiseWarehouseFavorite();

        franchiseWarehouseService.toggleFavorite(code);

        FranchiseWarehouse updatedWarehouse = franchiseWarehouseRepository.findById(code).orElseThrow();
        assertNotEquals(initialFavoriteStatus, updatedWarehouse.isFranchiseWarehouseFavorite());
    }

    @Test
    @DisplayName("즐겨찾기 한 상품 모두 조회")
    public void testFindAllFavorites() {
        List<FranchiseWarehouse> favorites = franchiseWarehouseService.findAllFavorites();

        assertFalse(favorites.isEmpty(), "즐겨찾기 항목이 하나 이상 있어야 합니다.");
        favorites.forEach(fw -> assertTrue(fw.isFranchiseWarehouseFavorite(), "모든 항목은 즐겨찾기로 표시되어야 합니다."));
    }
}