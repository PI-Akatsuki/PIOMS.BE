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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryFirstServiceTest {

    @Autowired
    private CategoryFirstService categoryFirstService;

    @Autowired
    private CategoryFirstRepository categoryFirstRepository;

    private CategoryFirst categoryFirst;
    private Admin admin;

    static RequestCategoryFirst request;


    @BeforeEach
    void init() {
        categoryFirst = new CategoryFirst();
        categoryFirst.setCategoryFirstCode(10);
        categoryFirst.setCategoryFirstName("test");
        categoryFirst.setCategoryFirstEnrollDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        categoryFirst.setCategoryFirstUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        categoryFirstRepository.save(categoryFirst);
    }

    @Test
    @DisplayName("카테고리(대) 전체 조회")
    void getAllCategoryFirst() {
        List<CategoryFirst> categoryFirstList = categoryFirstRepository.findAll();
        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.getAllCategoryFirst();

        assertEquals(categoryFirstList.size(), categoryFirstDTOS.size());
        System.out.println();
    }

    @Test
    @DisplayName("카테고리(대) 상세조회")
    void findCategoryFirstByCode() {
        int categoryFirstCode = 1;
        List<CategoryFirst> categoryFirstList = categoryFirstRepository.findByCategoryFirstCode(categoryFirstCode);
        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.findCategoryFirstByCode(categoryFirstCode);
        assertEquals(categoryFirstList.size(), categoryFirstDTOS.size());
    }



}
