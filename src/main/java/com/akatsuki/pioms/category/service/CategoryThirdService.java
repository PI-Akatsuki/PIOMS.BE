package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.category.vo.RequestCategoryPost;
import com.akatsuki.pioms.category.vo.ResponseCategoryPost;

import java.util.List;
import java.util.Optional;

public interface CategoryThirdService {
    List<CategoryThird> getAllCategoryThird();

    Optional<CategoryThird> findCategoryThirdByCode(int categoryThirdCode);

    ResponseCategoryPost postCategory(RequestCategoryPost request);
}
