package com.akatsuki.pioms.categorySecond.service;

import com.akatsuki.pioms.categorySecond.aggregate.*;

import java.util.List;
import java.util.Optional;

public interface CategorySecondService {
    List<CategorySecond> getAllCategorySecond();

    List<CategorySecond> getAllCategorySecondofFirst();

    Optional<CategorySecond> findCategorySecondByCode(int categorySecondCode);

    ResponseCategorySecondPost postCategorySecond(RequestCategorySecondPost request);

    ResponseCategorySecondUpdate updateCategorySecond(int categorySecondCode, RequestCategorySecondUpdate request);
}
