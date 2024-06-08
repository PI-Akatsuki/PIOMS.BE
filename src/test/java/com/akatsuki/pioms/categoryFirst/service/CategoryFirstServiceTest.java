package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstCreateDTO;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstUpdateDTO;
import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
import com.akatsuki.pioms.config.MockRedisConfig;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Import(MockRedisConfig.class)
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
    void modifyCategoryFirst() throws Exception {
        // Given
        int categoryFirstCode = categoryFirst.getCategoryFirstCode();
        CategoryFirstUpdateDTO categoryFirstUpdateDTO = new CategoryFirstUpdateDTO();
        categoryFirstUpdateDTO.setCategoryFirstName("Test updatedName");

        when(categoryFirstRepository.findById(anyInt())).thenReturn(Optional.of(categoryFirst));
        when(categoryFirstRepository.save(any(CategoryFirst.class))).thenReturn(categoryFirst);

        // When
        CategoryFirst result = categoryFirstService.modifyCategoryFirst(categoryFirstCode, categoryFirstUpdateDTO);

        // Verify logService call
        logService.saveLog("root", LogStatus.수정, result.getCategoryFirstName(), "CategoryFirst");

        // Then
        assertNotNull(result);
        assertEquals("Test updatedName", result.getCategoryFirstName());
    }
//        // Given
//        int categoryFirstCode = categoryFirst.getCategoryFirstCode();
//        CategoryFirstUpdateDTO categoryFirstUpdateDTO = new CategoryFirstUpdateDTO();
//        categoryFirstUpdateDTO.setCategoryFirstName("Test updatedName");
//
//        // When
//        CategoryFirst result = categoryFirstService.modifyCategoryFirst(categoryFirstCode, categoryFirstUpdateDTO);
//        logService.saveLog("root", LogStatus.수정,categoryFirst.getCategoryFirstName(),"CategoryFirst");
//
//        assertNotNull(result);
//        assertEquals("updatedName", result.getCategoryFirstName());

    @Test
    @DisplayName("카테고리(대) 삭제")
    @WithMockUser(username = "root", roles = {"ROOT"})
    void deleteCategoryFirst() throws Exception {
        // Given
        int categoryFirstCode = categoryFirst.getCategoryFirstCode();

        // Mocking findById to return the categoryFirst
        when(categoryFirstRepository.findById(anyInt())).thenReturn(Optional.of(categoryFirst));

        // Mocking the delete method
        doNothing().when(categoryFirstRepository).delete(any(CategoryFirst.class));

        // When
        ResponseEntity<String> response = categoryFirstService.deleteCategoryFirst(categoryFirstCode);
        logService.saveLog("root", LogStatus.삭제,categoryFirst.getCategoryFirstName(),"CategoryFirst");

        // Then
        assertNotNull(response);
        assertEquals("카테고리 소분류 삭제 완료", response.getBody());

    }
}
