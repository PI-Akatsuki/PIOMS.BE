package com.akatsuki.pioms.categorySecond.service;

import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import com.akatsuki.pioms.categorySecond.aggregate.ResponseCategorySecond;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondCreateDTO;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondDTO;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondUpdateDTO;
import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
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
class CategorySecondServiceTest {
    @MockBean
    private CategorySecondRepository categorySecondRepository;

    @Autowired
    private CategorySecondService categorySecondService;

    @MockBean
    private LogServiceImpl logService;

    private CategorySecond categorySecond;

    @BeforeEach
    void init() {
        categorySecond = new CategorySecond(1);
        categorySecondRepository.save(categorySecond);
    }


    @Test
    @DisplayName("카테고리(중) 전체 조회")
    void getAllCategorySecond() {
        List<CategorySecond> categorySecondList = categorySecondRepository.findAll();
        List<CategorySecondDTO> categorySecondDTOS = categorySecondService.getAllCategorySecond();
        assertEquals(categorySecondList.size(), categorySecondDTOS.size());
    }

    @Test
    @DisplayName("카테고리(중) 상세조회")
    void findCategorySecondByCode() {
        int categorySecondCode = 1;
        List<CategorySecond> categorySecondList = categorySecondRepository.findByCategorySecondCode(categorySecondCode);
        List<CategorySecondDTO> categorySecondDTOS = categorySecondService.findCategorySecondByCode(categorySecondCode);
        assertEquals(categorySecondList.size(), categorySecondDTOS.size());
    }

    @Test
    @DisplayName("카테고리(대)에 속한 카테고리(중) 상세 조회")
    void findCategorySecondInFirst() {
        int categoryFirstCode = 1;
        List<CategorySecond> categorySecondList = categorySecondRepository.findAllByCategoryFirstCategoryFirstCode(categoryFirstCode);
        List<ResponseCategorySecond> response = categorySecondService.getCategorySecondInFirst(categoryFirstCode);
        System.out.println("categorySecondList = " + categorySecondList);
        assertEquals(categorySecondList.size(), response.size());
    }

    @Test
    @DisplayName("카테고리(중) 신규 등록")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void createCategorySecond() {
        // Given
        CategorySecondCreateDTO categorySecondCreateDTO = new CategorySecondCreateDTO();
        categorySecondCreateDTO.setCategorySecondName("Test Name");

        // When
        CategorySecondDTO result = categorySecondService.createCategorySecond(categorySecondCreateDTO);
        logService.saveLog("root", LogStatus.등록,categorySecond.getCategorySecondName(),"CategorySecond");

        assertNotNull(result);
        assertEquals("Test Name", result.getCategorySecondName());
    }

    @Test
    @DisplayName("카테고리(중) 수정")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void modifyCategorySecond() throws Exception {
        // Given
        int categorySecondCode = categorySecond.getCategorySecondCode();
        CategorySecondUpdateDTO categorySecondUpdateDTO = new CategorySecondUpdateDTO();
        categorySecondUpdateDTO.setCategorySecondName("Test updatedName");

        when(categorySecondRepository.findById(anyInt())).thenReturn(Optional.of(categorySecond));
        when(categorySecondRepository.save(any(CategorySecond.class))).thenReturn(categorySecond);

        // When
        CategorySecond result = categorySecondService.modifyCategorySecond(categorySecondCode,categorySecondUpdateDTO);

        logService.saveLog("root", LogStatus.수정,categorySecond.getCategorySecondName(),"CategorySecond");

        // Then
        assertNotNull(result);
        assertEquals("Test updatedName", result.getCategorySecondName());
    }

    @Test
    @DisplayName("카테고리(중) 삭제")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void deleteCategorySecond() throws Exception {
        // Given
        int categorySecondCode = categorySecond.getCategorySecondCode();

        when(categorySecondRepository.findById(anyInt())).thenReturn(Optional.of(categorySecond));

        doNothing().when(categorySecondRepository).delete(any(CategorySecond.class));

        // When
        ResponseEntity<String> response = categorySecondService.deleteCategorySecond(categorySecondCode);
        logService.saveLog("root", LogStatus.삭제,categorySecond.getCategorySecondName(),"CategorySecond");

        // Then
        assertNotNull(response);
        assertEquals("카테고리(중) 삭제 완료", response.getBody());
    }
}
//    @Test
//    @DisplayName("카테고리(중) 신규 등록")
//    void postCategorySecond() {
//        RequestCategorySecond requestCategory = new RequestCategorySecond();
//        requestCategory.setCategorySecondCode(20);
//        requestCategory.setCategorySecondName("test");
//        requestCategory.setCategoryFirstCode(1);
//
//        ResponseEntity<String> postCategory = categorySecondService.postCategorySecond(requestCategory);
//        System.out.println("postCategory = " + postCategory);
//
//        assertNotNull(postCategory);
//        assertEquals("카테고리(중) 생성 완료!", postCategory.getBody());
//    }
//
//    @Test
//    @DisplayName("카테고리(중) 수정")
//    void updateCategorySecond() {
//        RequestCategorySecond requestCategory = new RequestCategorySecond();
//        requestCategory.setCategoryFirstCode(1);
//        requestCategory.setCategorySecondName("test11");
//
//        ResponseEntity<String> updateCategory = categorySecondService.updateCategorySecond(1, requestCategory);
//        System.out.println("updateCategory = " + updateCategory);
//
//        assertNotNull(updateCategory);
//        assertEquals("카테고리(중) 수정 완료!", updateCategory.getBody());
//    }
