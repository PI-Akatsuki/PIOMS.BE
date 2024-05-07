package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategorySecond;

import java.util.List;

public interface CategorySecondService {
    List<CategorySecond> getAllCategorySecond();

    List<CategorySecond> getAllCategorySecondofFirst();
}
