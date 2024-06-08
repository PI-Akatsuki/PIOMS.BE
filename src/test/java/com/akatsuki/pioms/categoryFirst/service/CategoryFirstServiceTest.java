//package com.akatsuki.pioms.categoryFirst.service;
//
//import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
//import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirst;
//import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
//import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class CategoryFirstServiceTest {
//
//    @Autowired
//    private CategoryFirstService categoryFirstService;
//
//    @MockBean
//    private CategoryFirstRepository categoryFirstRepository;
//
//    private CategoryFirst categoryFirst;
//
//
//    @BeforeEach
//    void init() {
//        categoryFirst = new CategoryFirst();
//        categoryFirst.setCategoryFirstCode(10);
//        categoryFirst.setCategoryFirstName("test");
//        categoryFirst.setCategoryFirstEnrollDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        categoryFirst.setCategoryFirstUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//
//        categoryFirstRepository.save(categoryFirst);
//    }
//
//    @Test
//    @DisplayName("카테고리(대) 전체 조회")
//    void getAllCategoryFirst() {
//        List<CategoryFirst> categoryFirstList = categoryFirstRepository.findAll();
//        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.getAllCategoryFirst();
//
//        assertEquals(categoryFirstList.size(), categoryFirstDTOS.size());
//        System.out.println();
//    }
//
//    @Test
//    @DisplayName("카테고리(대) 상세조회")
//    void findCategoryFirstByCode() {
//        int categoryFirstCode = 1;
//        List<CategoryFirst> categoryFirstList = categoryFirstRepository.findByCategoryFirstCode(categoryFirstCode);
//        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.findCategoryFirstByCode(categoryFirstCode);
//        assertEquals(categoryFirstList.size(), categoryFirstDTOS.size());
//    }
//
//    @Test
//    @DisplayName("카테고리(대) 신규 등록")
//    void postCategoryFirst() {
//        RequestCategoryFirst requestCategory = new RequestCategoryFirst();
//        requestCategory.setCategoryFirstCode(20);
//        requestCategory.setCategoryFirstName("test");
//
//        ResponseEntity<String> postCategory = categoryFirstService.postCategoryFirst(requestCategory);
//        System.out.println("postCategory = " + postCategory);
//
//        assertNotNull(postCategory);
//        assertEquals("카테고리(대) 생성 완료!", postCategory.getBody());
//    }
//
//    @Test
//    @DisplayName("카테고리(대) 수정")
//    void updateCategoryFirst() {
//
//        RequestCategoryFirst requestCategory = new RequestCategoryFirst();
//        requestCategory.setCategoryFirstName("test22");
//
//        ResponseEntity<String> updateCategory = categoryFirstService.updateCategoryFirst(1, requestCategory);
//        System.out.println("updateCategory = " + updateCategory);
//
//        assertNotNull(updateCategory);
//        assertEquals("카테고리(대) 수정 완료!", updateCategory.getBody());
//
//    }
//}
