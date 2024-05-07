package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategorySecond;
import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.category.repository.CategoryThirdDAO;
import com.akatsuki.pioms.category.vo.RequestCategoryPost;
import com.akatsuki.pioms.category.vo.RequestCategoryUpdate;
import com.akatsuki.pioms.category.vo.ResponseCategoryPost;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<CategoryThird> findCategoryThirdByCode(int categoryThirdCode) {
        return categoryThirdDAO.findById(categoryThirdCode);
    }
    @Override
    public ResponseCategoryPost postCategory(RequestCategoryPost request) {
        CategoryThird categoryThird = new CategoryThird();

        // CategorySecond 엔티티를 참조하는 필드에 해당 CategorySecond 엔티티를 설정
        CategorySecond categorySecond = new CategorySecond();
        categorySecond.setCategory_second_code(request.getCategory_second_code());
        categoryThird.setCategory_second_code(categorySecond);

        categoryThird.setCategory_third_name(request.getCategory_third_name());

        CategoryThird savedCategoryThird = categoryThirdDAO.save(categoryThird);

        ResponseCategoryPost responseValue = new ResponseCategoryPost(savedCategoryThird.getCategory_third_code(), savedCategoryThird.getCategory_third_name());
        return responseValue;
    }

    @Override
    public ResponseCategoryPost updateCategory(int categoryThirdCode, RequestCategoryUpdate request) {
        CategoryThird categoryThird = categoryThirdDAO.findById(categoryThirdCode)
                .orElseThrow(() -> new EntityNotFoundException("CategoryThird not found"));

        categoryThird.setCategory_third_name(request.getCategory_third_name());

        CategoryThird updatedCategoryThird = categoryThirdDAO.save(categoryThird);

        ResponseCategoryPost responseValue = new ResponseCategoryPost(updatedCategoryThird.getCategory_third_code(), updatedCategoryThird.getCategory_third_name());
        return responseValue;
    }


}
