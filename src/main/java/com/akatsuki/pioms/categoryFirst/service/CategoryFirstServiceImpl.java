package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirstPost;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirstUpdate;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirstPost;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirstUpdate;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    @Transactional
    public ResponseCategoryFirstUpdate updateCategoryFirst(int categoryFirstCode, RequestCategoryFirstUpdate request) {
        CategoryFirst categoryFirst = categoryFirstRepository.findById(categoryFirstCode)
                .orElseThrow(() -> new EntityNotFoundException("CategoryFirst not found"));

        CategoryFirst updatedCategoryFirst = categoryFirstRepository.save(categoryFirst);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        categoryFirst.setCategoryFirstName(request.getCategoryFirstName());
        categoryFirst.setCategoryFirstUpdateDate(formattedDateTime);

        ResponseCategoryFirstUpdate responseValue = new ResponseCategoryFirstUpdate(updatedCategoryFirst.getCategoryFirstCode(), updatedCategoryFirst.getCategoryFirstName(), updatedCategoryFirst.getCategoryFirstUpdateDate());
        return responseValue;
    }

    @Override
    @Transactional
    public ResponseCategoryFirstPost postCategoryFirst(RequestCategoryFirstPost request) {
        CategoryFirst categoryFirst = new CategoryFirst();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);


        categoryFirst.setCategoryFirstName(request.getCategoryFirstName());
        categoryFirst.setCategoryFirstEnrollDate(formattedDateTime);

        CategoryFirst savedCategoryFirst = categoryFirstRepository.save(categoryFirst);

        ResponseCategoryFirstPost responseValue = new ResponseCategoryFirstPost(savedCategoryFirst.getCategoryFirstCode(),savedCategoryFirst.getCategoryFirstName(), savedCategoryFirst.getCategoryFirstEnrollDate());
        return responseValue;
    }
}
