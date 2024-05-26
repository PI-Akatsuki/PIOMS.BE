//package com.akatsuki.pioms.categorySecond.service;
//
//import com.akatsuki.pioms.admin.aggregate.Admin;
//import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
//import com.akatsuki.pioms.categorySecond.aggregate.RequestCategorySecond;
//import com.akatsuki.pioms.categorySecond.aggregate.ResponseCategorySecond;
//import com.akatsuki.pioms.categorySecond.dto.CategorySecondDTO;
//import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class CategorySecondServiceTest {
//    @Autowired
//    private CategorySecondRepository categorySecondRepository;
//
//    @Autowired
//    private CategorySecondService categorySecondService;
//
//    static RequestCategorySecond request;
//
//    @Test
//    @DisplayName("카테고리(중) 전체 조회")
//    void getAllCategorySecond() {
//        List<CategorySecond> categorySecondList = categorySecondRepository.findAll();
//        List<CategorySecondDTO> categorySecondDTOS = categorySecondService.getAllCategorySecond();
//        assertEquals(categorySecondList.size(), categorySecondDTOS.size());
//    }
//
//    @Test
//    @DisplayName("카테고리(중) 상세조회")
//    void findCategorySecondByCode() {
//        int categorySecondCode = 1;
//        List<CategorySecond> categorySecondList = categorySecondRepository.findByCategorySecondCode(categorySecondCode);
//        List<CategorySecondDTO> categorySecondDTOS = categorySecondService.findCategorySecondByCode(categorySecondCode);
//        assertEquals(categorySecondList.size(), categorySecondDTOS.size());
//    }
//
//    @Test
//    @DisplayName("카테고리(대)에 속한 카테고리(중) 상세 조회")
//    void findCategorySecondInFirst() {
//        int categoryFirstCode = 1;
//        List<CategorySecond> categorySecondList = categorySecondRepository.findAllByCategoryFirstCategoryFirstCode(categoryFirstCode);
//        List<ResponseCategorySecond> response = categorySecondService.getCategorySecondInFirst(categoryFirstCode);
//        System.out.println("categorySecondList = " + categorySecondList);
//        assertEquals(categorySecondList.size(), response.size());
//    }
//
//    @Test
//    @DisplayName("카테고리(중) 신규 등록")
//    void postCategorySecond() {
//        RequestCategorySecond requestCategory = new RequestCategorySecond();
//        requestCategory.setCategorySecondCode(20);
//        requestCategory.setCategorySecondName("test");
//        requestCategory.setCategoryFirstCode(1);
//
//        ResponseEntity<String> postCategory = categorySecondService.postCategorySecond(requestCategory,1);
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
//        ResponseEntity<String> updateCategory = categorySecondService.updateCategorySecond(1, requestCategory, 1);
//        System.out.println("updateCategory = " + updateCategory);
//
//        assertNotNull(updateCategory);
//        assertEquals("카테고리(중) 수정 완료!", updateCategory.getBody());
//    }
//}
