package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategorySecond;
import com.akatsuki.pioms.category.repository.CategorySecondDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorySecondServiceImpl implements CategorySecondService{

    private final CategorySecondDAO categorySecondDAO;

    @Autowired
    public CategorySecondServiceImpl(CategorySecondDAO categorySecondDAO) {
        this.categorySecondDAO = categorySecondDAO;
    }

    @Override
    public List<CategorySecond> getAllCategorySecond() {
        return categorySecondDAO.findAll();
    }

    @Override
    public List<CategorySecond> getAllCategorySecondofFirst() {
        return categorySecondDAO.findAll();
    }

    @Override
    public Optional<CategorySecond> findCategorySecondByCode(int categorySecondCode) {
        return categorySecondDAO.findById(categorySecondCode);
    }
}
