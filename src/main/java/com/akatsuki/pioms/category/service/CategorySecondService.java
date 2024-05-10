package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategorySecond;
import com.akatsuki.pioms.category.vo.RequestCategorySecondPost;
import com.akatsuki.pioms.category.vo.RequestCategorySecondUpdate;
import com.akatsuki.pioms.category.vo.ResponseCategorySecondPost;
import com.akatsuki.pioms.category.vo.ResponseCategorySecondUpdate;

import java.util.List;
import java.util.Optional;

public interface CategorySecondService {
    List<CategorySecond> getAllCategorySecond();

    List<CategorySecond> getAllCategorySecondofFirst();

    Optional<CategorySecond> findCategorySecondByCode(int categorySecondCode);

    ResponseCategorySecondPost postCategorySecond(RequestCategorySecondPost request);

    ResponseCategorySecondUpdate updateCategorySecond(int categorySecondCode, RequestCategorySecondUpdate request);
}
