package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryFirst;
import com.akatsuki.pioms.category.repository.CategoryFirstDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryFirstServiceImpl implements CategoryFirstService {
    private final CategoryFirstDAO categoryFirstDAO;

    @Autowired
    public CategoryFirstServiceImpl(CategoryFirstDAO categoryFirstDAO) {
        this.categoryFirstDAO = categoryFirstDAO;
    }

    @Override
    public List<CategoryFirst> getAllCategoryFirst() {
        return categoryFirstDAO.findAll();
    }

    @Override
    public Optional<CategoryFirst> findCategoryFirstByCode(int categoryFirstCode) {
        return categoryFirstDAO.findById(categoryFirstCode);
    }
}
