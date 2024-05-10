package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryFirst;
import com.akatsuki.pioms.category.entity.CategorySecond;
import com.akatsuki.pioms.category.repository.CategorySecondRepository;
import com.akatsuki.pioms.category.vo.RequestCategorySecondPost;
import com.akatsuki.pioms.category.vo.RequestCategorySecondUpdate;
import com.akatsuki.pioms.category.vo.ResponseCategorySecondPost;
import com.akatsuki.pioms.category.vo.ResponseCategorySecondUpdate;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    @Transactional
    public ResponseCategorySecondPost postCategorySecond(RequestCategorySecondPost request) {
        CategorySecond categorySecond = new CategorySecond();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        CategoryFirst categoryFirst = new CategoryFirst();
        categoryFirst.setCategory_first_code(request.getCategory_first_code());
        categorySecond.setCategory_first_code(categoryFirst);

        categorySecond.setCategory_second_name(request.getCategory_second_name());
        categorySecond.setCategory_second_enroll_date(formattedDateTime);

        CategorySecond savedCategorySecond = categorySecondRepository.save(categorySecond);

        ResponseCategorySecondPost responseValue = new ResponseCategorySecondPost(savedCategorySecond.getCategory_second_code(), savedCategorySecond.getCategory_second_name(), savedCategorySecond.getCategory_second_enroll_date());
        return responseValue;
    }

    @Override
    @Transactional
    public ResponseCategorySecondUpdate updateCategorySecond(int categorySecondCode, RequestCategorySecondUpdate request) {
        CategorySecond categorySecond = categorySecondRepository.findById(categorySecondCode)
                .orElseThrow(() -> new EntityNotFoundException("CategorySecond not found"));

        CategorySecond updatedCategorySecond = categorySecondRepository.save(categorySecond);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        categorySecond.setCategory_second_name(request.getCategory_second_name());
        categorySecond.setCategory_second_update_date(formattedDateTime);

        ResponseCategorySecondUpdate responseValue = new ResponseCategorySecondUpdate(updatedCategorySecond.getCategory_second_code(), updatedCategorySecond.getCategory_second_name(), updatedCategorySecond.getCategory_second_update_date());
        return responseValue;
    }
}
