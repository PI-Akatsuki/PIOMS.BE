package com.akatsuki.pioms.categorySecond.service;

import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import com.akatsuki.pioms.categorySecond.aggregate.RequestCategorySecondPost;
import com.akatsuki.pioms.categorySecond.aggregate.RequestCategorySecondUpdate;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondDTO;
import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    static RequestCategorySecondPost postRequest;
    static RequestCategorySecondUpdate updateRequest;

    @Test
    @DisplayName("카테고리(중) 전체 조회")
    void getAllCategorySecond() {
        List<CategorySecond> categorySecondList = categorySecondRepository.findAll();
        List<CategorySecondDTO> categorySecondDTOS = categorySecondService.getAllCategorySecond();
        assertEquals(categorySecondList.size(), categorySecondDTOS.size());
    }

    @Test
    void findCategorySecondByCode() {assertEquals(true,true);}

    @Test
    void postCategorySecond() {assertEquals(true,true);}

    @Test
    void updateCategorySecond() {assertEquals(true,true);}
}