package com.akatsuki.pioms.categorySecond.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categorySecond.aggregate.*;
import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
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
        categoryFirst.setCategoryFirstCode(request.getCategoryFirstCode());
        categorySecond.setCategoryFirstCode(categoryFirst);

        categorySecond.setCategorySecondName(request.getCategorySecondName());
        categorySecond.setCategorySecondEnrollDate(formattedDateTime);

        CategorySecond savedCategorySecond = categorySecondRepository.save(categorySecond);

        ResponseCategorySecondPost responseValue = new ResponseCategorySecondPost(savedCategorySecond.getCategorySecondCode(), savedCategorySecond.getCategorySecondName(), savedCategorySecond.getCategorySecondEnrollDate());
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

        categorySecond.setCategorySecondName(request.getCategorySecondName());
        categorySecond.setCategorySecondUpdateDate(formattedDateTime);

        ResponseCategorySecondUpdate responseValue = new ResponseCategorySecondUpdate(updatedCategorySecond.getCategorySecondCode(), updatedCategorySecond.getCategorySecondName(), updatedCategorySecond.getCategorySecondUpdateDate());
        return responseValue;
    }
}
