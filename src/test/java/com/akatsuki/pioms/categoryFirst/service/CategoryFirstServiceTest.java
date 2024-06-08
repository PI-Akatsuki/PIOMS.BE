package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstCreateDTO;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstUpdateDTO;
import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryFirstServiceTest {

    @Autowired
    private CategoryFirstService categoryFirstService;

    @MockBean
    private CategoryFirstRepository categoryFirstRepository;

    @MockBean
    private LogServiceImpl logService;

    private CategoryFirst categoryFirst;


    @BeforeEach
    void init() {
        categoryFirst = new CategoryFirst(1) ;
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

    @Test
    @DisplayName("카테고리(대) 신규 등록")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void createCategoryFirst() {
        // Given
        CategoryFirstCreateDTO categoryFirstCreateDTO = new CategoryFirstCreateDTO();
        categoryFirstCreateDTO.setCategoryFirstName("Test Name");

        // When
        CategoryFirstDTO result = categoryFirstService.createCategoryFirst(categoryFirstCreateDTO);
        logService.saveLog("root", LogStatus.등록,categoryFirst.getCategoryFirstName(),"CategoryFirst");
        // Then
        assertNotNull(result);
        assertEquals("Test Name", result.getCategoryFirstName());
    }

    @Test
    @DisplayName("카테고리(대) 수정")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void modifyCategoryFirst() throws Exception{
        // Given
        int categoryFirstCode = categoryFirst.getCategoryFirstCode();
        CategoryFirstUpdateDTO categoryFirstUpdateDTO = new CategoryFirstUpdateDTO();
        categoryFirstUpdateDTO.setCategoryFirstName("Test updatedName");

        // When
        CategoryFirst result = categoryFirstService.modifyCategoryFirst(categoryFirstCode, categoryFirstUpdateDTO);
        logService.saveLog("root", LogStatus.수정,categoryFirst.getCategoryFirstName(),"CategoryFirst");

        assertNotNull(result);
        assertEquals("updatedName", result.getCategoryFirstName());
    }



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
}
