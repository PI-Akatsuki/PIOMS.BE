package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CategoryFirstServiceTest {

    @Autowired
    private CategoryFirstService categoryFirstService;

    @MockBean
    private CategoryFirstRepository categoryFirstRepository;

    @Autowired
    private MockMvc mockMvc;

//    public CategoryFirstServiceTest(CategoryFirstService categoryFirstService, CategoryFirstRepository categoryFirstRepository) {
//        this.categoryFirstService = categoryFirstService;
//        this.categoryFirstRepository = categoryFirstRepository;
//    }

    @BeforeEach
    void init() {

    }

    @Test
    @DisplayName("카테고리(대) 전체 조회")
    void getAllCategoryFirst() {
        List<CategoryFirst> categoryFirstList = categoryFirstRepository.findAll();
        List<CategoryFirstDTO> categoryFirstDTOS = categoryFirstService.getAllCategoryFirst();

        assertEquals(categoryFirstList.size(), categoryFirstDTOS.size());

    }

    @Test
    void findCategoryFirstByCode() {assertEquals(true,true);}

    @Test
    void postCategoryFirst() throws Exception {

//        int categoryFirstCode = 10;
//        Map<String, String> input = new HashMap<>();
//
//        input.put("categoryFirstName", "test");
//        input.put("categoryFirstEnrollDate", "2024-05-16 22:32:00");
//        input.put("categoryFirstUpdateDate", "2024-05-16 22:32:00");
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/category/first/post"))
//                .con

    }

    @Test
    void updateCategoryFirst() {assertEquals(true,true);}

}
