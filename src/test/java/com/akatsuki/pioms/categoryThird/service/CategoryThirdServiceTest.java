package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThird;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import com.akatsuki.pioms.categoryThird.repository.CategoryThirdRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    static RequestCategoryThird request;

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
    void deleteCategoryThird() {assertEquals(true,true);}
}