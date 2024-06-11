package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThird;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdCreateDTO;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdUpdateDTO;
import com.akatsuki.pioms.categoryThird.repository.CategoryThirdRepository;
import com.akatsuki.pioms.config.MockRedisConfig;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Import(MockRedisConfig.class)
@Transactional
class CategoryThirdServiceTest {

    @Autowired
    private CategoryThirdService categoryThirdService;

    @MockBean
    private CategoryThirdRepository categoryThirdRepository;

    @MockBean
    private LogServiceImpl logService;

    private CategoryThird categoryThird;

    @BeforeEach
    void init() {
        categoryThird = new CategoryThird();
        categoryThirdRepository.save(categoryThird);
    }

    @Test
    @DisplayName("카테고리(소) 전체 조회")
    void getAllCategoryThird() {
        List<CategoryThird> categoryThirdList = categoryThirdRepository.findAll();
        List<CategoryThirdDTO> categoryThirdDTOS = categoryThirdService.getAllCategoryThird();
        assertEquals(categoryThirdList.size(), categoryThirdDTOS.size());
    }

    @Test
    @DisplayName("카테고리(소) 상세 조회")
    void findCategoryThirdByCode() {
        int categoryThirdCode = 1;
        List<CategoryThird> categoryThirdList = categoryThirdRepository.findByCategoryThirdCode(categoryThirdCode);
        List<CategoryThirdDTO> categoryThirdDTOS = categoryThirdService.findCategoryThirdByCode(categoryThirdCode);
        assertEquals(categoryThirdList.size(), categoryThirdDTOS.size());
    }

    @Test
    @DisplayName("카테고리(중)에 속한 카테고리(소) 상세 조회")
    void findCategoryThirdInSecond() {
        int categorySecondCode = 1;
        List<CategoryThird> categoryThirdList = categoryThirdRepository.findAllByCategorySecondCategorySecondCode(categorySecondCode);
        List<ResponseCategoryThird> response = categoryThirdService.getCategoryThirdInSecond(categorySecondCode);
        System.out.println("categoryThirdList = " + categoryThirdList);
        assertEquals(categoryThirdList.size(), response.size());
    }

    @Test
    @DisplayName("카테고리(소) 신규 등록")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void createCategoryThird() {
        // Given
        CategoryThirdCreateDTO categoryThirdCreateDTO = new CategoryThirdCreateDTO();
        categoryThirdCreateDTO.setCategoryThirdName("Test Name");

        // When
        CategoryThirdDTO result = categoryThirdService.createCategoryThird(categoryThirdCreateDTO);
        logService.saveLog("root", LogStatus.등록, categoryThird.getCategoryThirdName(), "CategoryThird");

        assertNotNull(result);
        assertEquals("Test Name", result.getCategoryThirdName());
    }

    @Test
    @DisplayName("카테고리(소) 수정")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void modifyCategoryThird() throws Exception {
        // Given
        int categoryThirdCode = categoryThird.getCategoryThirdCode();
        CategoryThirdUpdateDTO categoryThirdUpdateDTO = new CategoryThirdUpdateDTO();
        categoryThirdUpdateDTO.setCategoryThirdName("Test updatedName");

        when(categoryThirdRepository.findById(anyInt())).thenReturn(Optional.of(categoryThird));
        when(categoryThirdRepository.save(any(CategoryThird.class))).thenReturn(categoryThird);

        // When
        CategoryThird result = categoryThirdService.modifyCategoryThird(categoryThirdCode, categoryThirdUpdateDTO);

        logService.saveLog("root", LogStatus.수정,categoryThird.getCategoryThirdName(),"CategoryThird");

        // Then
        assertNotNull(result);
        assertEquals("Test updatedName", result.getCategoryThirdName());
    }

    @Test
    @DisplayName("카테고리(소) 삭제")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void deleteCategoryThird() throws Exception {
        // Given
        int categoryThirdCode = categoryThird.getCategoryThirdCode();

        when(categoryThirdRepository.findById(anyInt())).thenReturn(Optional.of(categoryThird));

        doNothing().when(categoryThirdRepository).delete(any(CategoryThird.class));

        // When
        ResponseEntity<String> response = categoryThirdService.deleteCategoryThird(categoryThirdCode);
        logService.saveLog("root", LogStatus.삭제,categoryThird.getCategoryThirdName(),"CategoryThird");

        // Then
        assertNotNull(response);
        assertEquals("카테고리(소) 삭제 완료", response.getBody());

    }
}
//    @Test
//    @DisplayName("카테고리(소) 신규 등록")
//    void postCategoryThird() {
//        RequestCategoryThird requestCategory = new RequestCategoryThird();
//        requestCategory.setCategoryThirdCode(20);
//        requestCategory.setCategoryThirdName("test");
//        requestCategory.setCategorySecondCode(1);
//
//        ResponseEntity<String> postCategory = categoryThirdService.postCategory(requestCategory);
//        System.out.println("postCategory = " + postCategory);
//
//        assertNotNull(postCategory);
//        assertEquals("카테고리(소) 생성 완료!", postCategory.getBody());
//    }
//
//    @Test
//    @DisplayName("카테고리(소) 수정")
//    void updateCategoryThird() {
//        RequestCategoryThird requestCategory = new RequestCategoryThird();
//        requestCategory.setCategorySecondCode(1);
//        requestCategory.setCategoryThirdName("test00");
//
//        ResponseEntity<String> updateCategory = categoryThirdService.updateCategory(1, requestCategory);
//        System.out.println("updateCategory = " + updateCategory);
//
//        assertNotNull(updateCategory);
//        assertEquals("카테고리(소) 수정 완료!", updateCategory.getBody());
//    }
//
//    @Test
//    void deleteCategory() {
//        // Given: 새로운 카테고리를 생성합니다.
//        RequestCategoryThird requestCategory = new RequestCategoryThird();
//        requestCategory.setCategoryThirdCode(20);
//        requestCategory.setCategoryThirdName("test");
//        requestCategory.setCategorySecondCode(1);
//
//        ResponseEntity<String> postCategory = categoryThirdService.postCategory(requestCategory);
//
//        // When: 해당 카테고리를 삭제합니다.
//        categoryThirdService.deleteCategoryThird(20);
//
//        // Then: 삭제된 카테고리가 데이터베이스에 없는지 확인합니다.
//        List<ResponseCategoryThird> categoryThirdList = categoryThirdService.getCategoryThirdInSecond(requestCategory.getCategorySecondCode());
//        boolean categoryExists = categoryThirdList.stream()
//                .anyMatch(category -> category.getCategoryThirdCode() == requestCategory.getCategoryThirdCode());
//        assertFalse(categoryExists, "삭 성");
//
//    }
