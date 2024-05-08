package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategorySecond;
import com.akatsuki.pioms.category.repository.CategorySecondRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorySecondServiceImpl implements CategorySecondService{

    private final CategorySecondRepository categorySecondRepository;

    @Autowired
    public CategorySecondServiceImpl(CategorySecondRepository categorySecondRepository) {
        this.categorySecondRepository = categorySecondRepository;
    }

    @Override
    public List<CategorySecond> getAllCategorySecond() {
        return categorySecondRepository.findAll();
    }

    @Override
    public List<CategorySecond> getAllCategorySecondofFirst() {
        return categorySecondRepository.findAll();
    }

    @Override
    public Optional<CategorySecond> findCategorySecondByCode(int categorySecondCode) {
        return categorySecondRepository.findById(categorySecondCode);
    }
}
