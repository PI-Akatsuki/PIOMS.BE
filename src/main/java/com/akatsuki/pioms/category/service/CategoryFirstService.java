package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryFirst;
import com.akatsuki.pioms.category.vo.RequestCategoryFirstPost;
import com.akatsuki.pioms.category.vo.RequestCategoryFirstUpdate;
import com.akatsuki.pioms.category.vo.ResponseCategoryFirstPost;
import com.akatsuki.pioms.category.vo.ResponseCategoryFirstUpdate;

import java.util.List;
import java.util.Optional;

public interface CategoryFirstService {
    List<CategoryFirst> getAllCategoryFirst();

    Optional<CategoryFirst> findCategoryFirstByCode(int categoryFirstCode);

    ResponseCategoryFirstUpdate updateCategoryFirst(int categoryFirstCode, RequestCategoryFirstUpdate request);

    ResponseCategoryFirstPost postCategoryFirst(RequestCategoryFirstPost request);
}
