package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstCreateDTO;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstUpdateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryFirstService {
    List<CategoryFirstDTO> getAllCategoryFirst();

    List<CategoryFirstDTO> findCategoryFirstByCode(int categoryFirstCode);

    ResponseEntity<String> updateCategoryFirst(int categoryFirstCode, RequestCategoryFirst request);

    ResponseEntity<String> postCategoryFirst(RequestCategoryFirst request);

    ResponseEntity<String> deleteCategoryFirst(int categoryFirstCode);

    CategoryFirstDTO createCategoryFirst(CategoryFirstCreateDTO categoryFirstCreateDTO );
    CategoryFirst modifyCategoryFirst(int categoryFirstCode, CategoryFirstUpdateDTO categoryFirstUpdateDTO);
}
