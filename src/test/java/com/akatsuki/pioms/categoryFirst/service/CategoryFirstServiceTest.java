package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryFirstServiceTest {

    @Autowired
    private CategoryFirstService categoryFirstService;

    @Autowired
    private CategoryFirstRepository categoryFirstRepository;

    static RequestCategoryFirst request;

    @BeforeEach
    void init() {
        request = new RequestCategoryFirst("테스트");
    }

    @Test
    @DisplayName("카테고리(대) 전체 조회")
    void getAllCategoryFirst() {
        List<CategoryFirst> categoryFirstList = categoryFirstRepository.findAll();
        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.getAllCategoryFirst();

        assertEquals(categoryFirstList.size(), categoryFirstDTOS.size());

    }

    @Test
    @DisplayName("카테고리(대) 상세조회")
    void findCategoryFirstByCode() {
        int categoryFirstCode = 1;
        List<CategoryFirst> categoryFirstList = categoryFirstRepository.findByCategoryFirstCode(categoryFirstCode);
        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.findCategoryFirstByCode(categoryFirstCode);
        assertEquals(categoryFirstList.size(), categoryFirstDTOS.size());
    }

//    @Test
//    @DisplayName("카테고리(대) 신규 등록")
//    void postCategoryFirst() {
//        Admin requestorAdmin = new Admin();
//        requestorAdmin.setAdminCode(1);
//
//        CategoryFirst categoryFirst = new CategoryFirst();
//        categoryFirst.setCategoryFirstName("postTest");
//        categoryFirst.setCategoryFirstEnrollDate("2024-05-17 00:00:00");
//        categoryFirst.setCategoryFirstUpdateDate("2024-05-17 00:00:00");
//
//        ResponseEntity<String> response = categoryFirstService.postCategoryFirst(request, categoryFirst.getCategoryFirstCode());
//
//        assertEquals("신규 카테고리 등록",response.getBody());
//    }
//
//    @Test
//    @DisplayName("카테고라(대) 수정")
//    void updateCategoryFirst() {
//        Admin requestorAdmin = new Admin();
//        requestorAdmin.setAdminCode(1);
//
//        CategoryFirst categoryFirst = new CategoryFirst();
//        categoryFirst.setCategoryFirstName("updateTest");
//        categoryFirst.setCategoryFirstEnrollDate("2024-05-17 00:00:00");
//        categoryFirst.setCategoryFirstUpdateDate("2024-05-17 00:00:00");
//
//        ResponseEntity<String> response = categoryFirstService.updateCategoryFirst(2, request, 1);
//
//        assertEquals("카테고리 수정",response.getBody());
//    }

}
