package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryFirst;

import java.util.List;
import java.util.Optional;

public interface CategoryFirstService {
    List<CategoryFirst> getAllCategoryFirst();

    Optional<CategoryFirst> findCategoryFirstByCode(int categoryFirstCode);
}
