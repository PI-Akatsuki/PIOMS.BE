package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.category.repository.CategorySecondDAO;
import com.akatsuki.pioms.category.repository.CategoryThirdDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryThirdServiceImpl implements CategoryThirdService{

    private final CategoryThirdDAO categoryThirdDAO;
    private final CategorySecondDAO categorySecondDAO;


    @Autowired
    public CategoryThirdServiceImpl(CategoryThirdDAO categoryThirdDAO, CategorySecondDAO categorySecondDAO) {
        this.categoryThirdDAO = categoryThirdDAO;
        this.categorySecondDAO = categorySecondDAO;
    }

    @Override
    public List<CategoryThird> getAllCategoryThird() {
        return categoryThirdDAO.findAll();
    }

    @Override
    public Optional<CategoryThird> findCategoryThirdByCode(int categoryThirdCode) {
        return categoryThirdDAO.findById(categoryThirdCode);
    }



//    @Override
//    public ResponseCategoryPost postCategoryThird(int categorySecondCode, String categoryThirdName) {
//        CategorySecond categorySecond = findCategorySecondByCode(categorySecondCode);
//
//        CategoryThird categoryThird = new CategoryThird();
//        categoryThird.setCategory_third_name(categoryThirdName);
//        categoryThird.setCategory_second_code(categorySecond);
//
//        CategoryThird savedCategoryThird = categoryThirdDAO.save(categoryThird);
//
//        ResponseCategoryPost response =
//                new ResponseCategoryPost(
//                        savedCategoryThird.getCategory_third_code(),
//                        savedCategoryThird.getCategory_third_name());
//        return response;
//    }
//
//    @Override
//    public CategorySecond findCategorySecondByCode(int categorySecondCode) {
//        return categorySecondDAO.findByCode(categorySecondCode);
//    }

}
