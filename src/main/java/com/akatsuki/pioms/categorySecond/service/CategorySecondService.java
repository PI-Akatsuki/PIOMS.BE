package com.akatsuki.pioms.categorySecond.service;

import com.akatsuki.pioms.categorySecond.aggregate.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategorySecondService {
    List<CategorySecond> getAllCategorySecond();

    CategorySecond findCategorySecondByCode(int categorySecondCode);

    ResponseEntity<String> postCategorySecond(RequestCategorySecondPost request, int requesterAdminCode);

    ResponseEntity<String> updateCategorySecond(int categorySecondCode, RequestCategorySecondUpdate request, int requesterAdminCode);
}
