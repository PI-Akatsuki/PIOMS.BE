package com.akatsuki.pioms.categorySecond.service;

import com.akatsuki.pioms.categorySecond.aggregate.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CategorySecondService {
    List<CategorySecond> getAllCategorySecond();

    Optional<CategorySecond> findCategorySecondByCode(int categorySecondCode);

    ResponseEntity<String> postCategorySecond(RequestCategorySecondPost request/*, int requesterAdminCode*/);

    ResponseCategorySecondUpdate updateCategorySecond(int categorySecondCode, RequestCategorySecondUpdate request/*, int requesterAdminCode*/);
}
