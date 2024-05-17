package com.akatsuki.pioms.categorySecond.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import com.akatsuki.pioms.categorySecond.aggregate.RequestCategorySecond;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondDTO;
import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategorySecondServiceTest {
    @Autowired
    private CategorySecondRepository categorySecondRepository;

    @Autowired
    private CategorySecondService categorySecondService;

    static RequestCategorySecond request;

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
        assertEquals(categorySecondList.size(),categorySecondDTOS.size());
    }

//    @Test
//    void postCategorySecond() {
//        Admin requestorAdmin = new Admin();
//        requestorAdmin.setAdminCode(1);
//
//        CategorySecond categorySecond = new CategorySecond();
//        categorySecond.setCategorySecondName("postTest");
//        categorySecond.setCategorySecondEnrollDate("2024-05-17 00:00:00");
//        categorySecond.setCategorySecondUpdateDate("2024-05-17 00:00:00");
//        categorySecond.setCategoryFirstCode(1);
//
//        ResponseEntity<String> response = categorySecondService.postCategorySecond(request, categorySecond.getCategorySecondCode());
//
//        assertEquals("신규 카테고리 등록", response.getBody());
    }

//    @Test
//    void updateCategorySecond() {
//        Admin requestorAdmin = new Admin();
//        requestorAdmin.setAdminCode(1);
//
//        CategorySecond categorySecond = new CategorySecond();
//        request.setCategorySecondCode(1);
//        request.setCategorySecondName("postTest");
//        categorySecond.setCategorySecondEnrollDate("2024-05-17 00:00:00");
//        categorySecond.setCategorySecondUpdateDate("2024-05-17 00:00:00");
//        categorySecond.setCategoryFirstCode(1);
//
//        ResponseEntity<String> response = categorySecondService.updateCategorySecond(1, request,1);
//    }
//}