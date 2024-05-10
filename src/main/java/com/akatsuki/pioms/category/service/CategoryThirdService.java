package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.category.vo.RequestCategoryThirdPost;
import com.akatsuki.pioms.category.vo.RequestCategoryThirdUpdate;
import com.akatsuki.pioms.category.vo.ResponseCategoryThirdPost;

import java.util.List;
import java.util.Optional;

public interface CategoryThirdService {
    List<CategoryThird> getAllCategoryThird();

    Optional<CategoryThird> findCategoryThirdByCode(int categoryThirdCode);

    ResponseCategoryThirdPost postCategory(RequestCategoryThirdPost request);

    ResponseCategoryThirdPost updateCategory(int categoryThirdCode, RequestCategoryThirdUpdate request);



}
