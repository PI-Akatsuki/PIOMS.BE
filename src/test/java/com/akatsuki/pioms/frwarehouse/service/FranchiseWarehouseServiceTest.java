package com.akatsuki.pioms.frwarehouse.service;

import com.akatsuki.pioms.config.MockRedisConfig;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.repository.FranchiseRepository;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
import com.akatsuki.pioms.frwarehouse.repository.FranchiseWarehouseRepository;
import com.akatsuki.pioms.product.aggregate.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Import(MockRedisConfig.class)
@Transactional
class FranchiseWarehouseServiceTests {

    @MockBean
    private FranchiseWarehouseRepository franchiseWarehouseRepository;

    @MockBean
    private FranchiseRepository franchiseRepository;

    @MockBean
    private FranchiseOwnerRepository franchiseOwnerRepository;

    @Autowired
    private FranchiseWarehouseServiceImpl franchiseWarehouseService;

    private FranchiseWarehouse franchiseWarehouse;
    private Franchise franchise;
    private FranchiseOwner franchiseOwner;
    private Product product;

    @BeforeEach
    void setUp() {
        franchiseOwner = new FranchiseOwner();
        franchiseOwner.setFranchiseOwnerCode(1);
        franchiseOwner.setFranchiseOwnerId("가맹점주");
        franchiseOwner.setFranchiseOwnerPwd("password");
        franchiseOwner.setFranchiseOwnerName("김가맹");
        franchiseOwner.setFranchiseOwnerEmail("test@test.com");
        franchiseOwner.setFranchiseOwnerPhone("0105656565");
        franchiseOwner.setFranchiseOwnerEnrollDate("20210506");

        franchise = new Franchise();
        franchise.setFranchiseCode(1);
        franchise.setFranchiseOwner(franchiseOwner);
        franchise.setFranchiseName("Test Franchise");

        franchiseOwner.setFranchise(franchise); // Setting the franchise for the franchiseOwner

        product = new Product();
        product.setProductCode(1);
        product.setProductName("Test Product");

        franchiseWarehouse = new FranchiseWarehouse();
        franchiseWarehouse.setFranchiseWarehouseCode(1);
        franchiseWarehouse.setFranchiseCode(1);
        franchiseWarehouse.setFranchiseWarehouseCount(10);
        franchiseWarehouse.setFranchiseWarehouseEnable(10);
        franchiseWarehouse.setFranchiseWarehouseTotal(20);
        franchiseWarehouse.setProduct(product); // Setting the product for the warehouse
    }

    @DisplayName("즐겨찾기 토글 테스트")
    @Test
    void toggleFavoriteTest() {
        // Given
        franchiseWarehouse.setFranchiseWarehouseFavorite(false);
        when(franchiseWarehouseRepository.findById(1)).thenReturn(Optional.of(franchiseWarehouse));

        // When
        franchiseWarehouseService.toggleFavorite(1);

        // Then
        assertTrue(franchiseWarehouse.isFranchiseWarehouseFavorite());
        verify(franchiseWarehouseRepository, times(1)).save(franchiseWarehouse);
    }

    @DisplayName("즐겨찾기 제거 테스트")
    @Test
    void removeFavoriteTest() {
        // Given
        franchiseWarehouse.setFranchiseWarehouseFavorite(true);
        when(franchiseWarehouseRepository.findById(1)).thenReturn(Optional.of(franchiseWarehouse));

        // When
        franchiseWarehouseService.removeFavorite(1);

        // Then
        assertFalse(franchiseWarehouse.isFranchiseWarehouseFavorite());
        verify(franchiseWarehouseRepository, times(1)).save(franchiseWarehouse);
    }

    @DisplayName("전체 즐겨찾기 조회 테스트")
    @Test
    void findAllFavoritesTest() {
        // Given
        when(franchiseWarehouseRepository.findByFranchiseWarehouseFavoriteTrue())
                .thenReturn(List.of(franchiseWarehouse));

        // When
        List<FranchiseWarehouse> result = franchiseWarehouseService.findAllFavorites();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(franchiseWarehouse.getFranchiseWarehouseCode(), result.get(0).getFranchiseWarehouseCode());
    }

    @DisplayName("가맹점주 코드로 제품 조회 테스트")
    @Test
    void getProductsByFranchiseOwnerCodeTest() {
        // Given
        when(franchiseRepository.findByFranchiseOwnerFranchiseOwnerCode(1)).thenReturn(franchise);
        when(franchiseWarehouseRepository.findByFranchiseCode(1)).thenReturn(List.of(franchiseWarehouse));

        // When
        List<FranchiseWarehouseDTO> result = franchiseWarehouseService.getProductsByFranchiseOwnerCode(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(franchiseWarehouse.getFranchiseWarehouseCode(), result.get(0).getFranchiseWarehouseCode());
    }

    @DisplayName("가맹점주 즐겨찾기 조회 테스트")
    @Test
    void findFavoritesByOwnerTest() {
        // Given
        franchiseWarehouse.setFranchiseWarehouseFavorite(true);
        when(franchiseOwnerRepository.findById(1)).thenReturn(Optional.of(franchiseOwner));
        when(franchiseWarehouseRepository.findByFranchiseCodeAndFranchiseWarehouseFavorite(1, true))
                .thenReturn(List.of(franchiseWarehouse));

        // When
        List<FranchiseWarehouseDTO> result = franchiseWarehouseService.findFavoritesByOwner(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(franchiseWarehouse.getFranchiseWarehouseCode(), result.get(0).getFranchiseWarehouseCode());
        assertTrue(result.get(0).isFranchiseWarehouseFavorite());
    }
}
