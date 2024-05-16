package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirstPost;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirstUpdate;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirstPost;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirstUpdate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryFirstService {
    List<CategoryFirst> getAllCategoryFirst();

    CategoryFirst findCategoryFirstByCode(int categoryFirstCode);

    ResponseEntity<String> updateCategoryFirst(int categoryFirstCode, RequestCategoryFirstUpdate request, int requesterAdminCode);

    ResponseEntity<String> postCategoryFirst(RequestCategoryFirstPost request, int requesterAdminCode);
}
