package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.dto.CategoryThirdDTO;
import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.category.vo.RequestCategoryPost;
import com.akatsuki.pioms.category.vo.RequestCategoryUpdate;
import com.akatsuki.pioms.category.vo.ResponseCategoryPost;
import com.akatsuki.pioms.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface CategoryThirdService {
    List<CategoryThird> getAllCategoryThird();

    Optional<CategoryThird> findCategoryThirdByCode(int categoryThirdCode);

    ResponseCategoryPost postCategory(RequestCategoryPost request);

    ResponseCategoryPost updateCategory(int categoryThirdCode, RequestCategoryUpdate request);



}
