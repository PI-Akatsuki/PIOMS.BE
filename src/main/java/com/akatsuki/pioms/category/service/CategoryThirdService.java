package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryThird;

import java.util.List;
import java.util.Optional;

public interface CategoryThirdService {
    List<CategoryThird> getAllCategoryThird();

    Optional<CategoryThird> findCategoryThirdByCode(int categoryThirdCode);

//    ResponseCategoryPost postCategoryThird(int categorySecondCode, String categoryThirdName);
}
