package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirstPost;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirstUpdate;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirstPost;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirstUpdate;

import java.util.List;
import java.util.Optional;

public interface CategoryFirstService {
    List<CategoryFirst> getAllCategoryFirst();

    Optional<CategoryFirst> findCategoryFirstByCode(int categoryFirstCode);

    ResponseCategoryFirstUpdate updateCategoryFirst(int categoryFirstCode, RequestCategoryFirstUpdate request/*, int requesterAdminCode*/);

    ResponseCategoryFirstPost postCategoryFirst(RequestCategoryFirstPost request/*, int requesterAdminCode*/);
}
