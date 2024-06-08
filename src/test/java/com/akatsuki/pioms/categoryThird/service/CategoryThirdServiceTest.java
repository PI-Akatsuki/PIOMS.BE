package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThird;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import com.akatsuki.pioms.categoryThird.repository.CategoryThirdRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryThirdServiceTest {

    @Autowired
    private CategoryThirdService categoryThirdService;

    @Autowired
    private CategoryThirdRepository categoryThirdRepository;

    @MockBean
    private MockMvc mvc;

    static RequestCategoryThird request;
    private RequestBuilder delete;


    @Test
    @DisplayName("카테고리(소) 전체 조회")
    void getAllCategoryThird() {
        List<CategoryThird> categoryThirdList = categoryThirdRepository.findAll();
        List<CategoryThirdDTO> categoryThirdDTOS = categoryThirdService.getAllCategoryThird();
        assertEquals(categoryThirdList.size(), categoryThirdDTOS.size());
    }

    @Test
    @DisplayName("카테고리(중) 상세 조회")
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
    void postCategoryThird() {
        RequestCategoryThird requestCategory = new RequestCategoryThird();
        requestCategory.setCategoryThirdCode(20);
        requestCategory.setCategoryThirdName("test");
        requestCategory.setCategorySecondCode(1);

        ResponseEntity<String> postCategory = categoryThirdService.postCategory(requestCategory, 1);
        System.out.println("postCategory = " + postCategory);

        assertNotNull(postCategory);
        assertEquals("카테고리(소) 생성 완료!", postCategory.getBody());
    }

    @Test
    @DisplayName("카테고리(소) 수정")
    void updateCategoryThird() {
        RequestCategoryThird requestCategory = new RequestCategoryThird();
        requestCategory.setCategorySecondCode(1);
        requestCategory.setCategoryThirdName("test00");

        ResponseEntity<String> updateCategory = categoryThirdService.updateCategory(1, requestCategory, 1);
        System.out.println("updateCategory = " + updateCategory);

        assertNotNull(updateCategory);
        assertEquals("카테고리(소) 수정 완료!", updateCategory.getBody());
    }

    @Test
    void deleteCategory() {
        // Given: 새로운 카테고리를 생성합니다.
        RequestCategoryThird requestCategory = new RequestCategoryThird();
        requestCategory.setCategoryThirdCode(20);
        requestCategory.setCategoryThirdName("test");
        requestCategory.setCategorySecondCode(1);

        ResponseEntity<String> postCategory = categoryThirdService.postCategory(requestCategory, 1);

        // When: 해당 카테고리를 삭제합니다.
        categoryThirdService.deleteCategoryThird(20, 1);

        // Then: 삭제된 카테고리가 데이터베이스에 없는지 확인합니다.
        List<ResponseCategoryThird> categoryThirdList = categoryThirdService.getCategoryThirdInSecond(requestCategory.getCategorySecondCode());
        boolean categoryExists = categoryThirdList.stream()
                .anyMatch(category -> category.getCategoryThirdCode() == requestCategory.getCategoryThirdCode());
        assertFalse(categoryExists, "삭 성");

    }
}
