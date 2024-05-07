package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.category.vo.ResponseCategoryPost;

import java.util.List;

public interface CategoryThirdService {
    List<CategoryThird> getAllCategoryThird();

    List<CategoryThird> getAllCategoryThirdofSecond();

//    ResponseCategoryPost postCategoryThird(CategoryThird categoryThird);
}
