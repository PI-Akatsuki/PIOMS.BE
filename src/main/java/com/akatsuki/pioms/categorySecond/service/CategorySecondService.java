package com.akatsuki.pioms.categorySecond.service;

import com.akatsuki.pioms.categorySecond.aggregate.*;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategorySecondService {
    List<CategorySecondDTO> getAllCategorySecond();

    List<CategorySecondDTO> findCategorySecondByCode(int categorySecondCode);

    ResponseEntity<String> postCategorySecond(RequestCategorySecond request);

    ResponseEntity<String> updateCategorySecond(int categorySecondCode, RequestCategorySecond request);

    List<ResponseCategorySecond> getCategorySecondInFirst(int categoryFirstCode);

    ResponseEntity<String> deleteCategorySecond(int categorySecondCode);
}
