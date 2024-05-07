package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.category.repository.CategoryThirdDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryThirdServiceImpl implements CategoryThirdService{

    private final CategoryThirdDAO categoryThirdDAO;

    @Autowired
    public CategoryThirdServiceImpl(CategoryThirdDAO categoryThirdDAO) {
        this.categoryThirdDAO = categoryThirdDAO;
    }

    @Override
    public List<CategoryThird> getAllCategoryThird() {
        return categoryThirdDAO.findAll();
    }

    @Override
    public List<CategoryThird> getAllCategoryThirdofSecond() {
        return categoryThirdDAO.findAll();
    }

//    @Override
//    public
}
