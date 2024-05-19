package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryFirstService {
    List<CategoryFirstDTO> getAllCategoryFirst();

    List<CategoryFirstDTO> findCategoryFirstByCode(int categoryFirstCode);

    ResponseEntity<String> updateCategoryFirst(int categoryFirstCode, RequestCategoryFirst request/*, int requesterAdminCode*/);

    ResponseEntity<String> postCategoryFirst(RequestCategoryFirst request/*, int requesterAdminCode*/);
}
