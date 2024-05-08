package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryFirst;
import com.akatsuki.pioms.category.repository.CategoryFirstRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryFirstServiceImpl implements CategoryFirstService {
    private final CategoryFirstRepository categoryFirstRepository;

    @Autowired
    public CategoryFirstServiceImpl(CategoryFirstRepository categoryFirstRepository) {
        this.categoryFirstRepository = categoryFirstRepository;
    }

    @Override
    public List<CategoryFirst> getAllCategoryFirst() {
        return categoryFirstRepository.findAll();
    }

    @Override
    public Optional<CategoryFirst> findCategoryFirstByCode(int categoryFirstCode) {
        return categoryFirstRepository.findById(categoryFirstCode);
    }
}
