package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.category.entity.CategoryFirst;
import com.akatsuki.pioms.category.repository.CategoryFirstRepository;
import com.akatsuki.pioms.category.vo.RequestCategoryFirstPost;
import com.akatsuki.pioms.category.vo.RequestCategoryFirstUpdate;
import com.akatsuki.pioms.category.vo.ResponseCategoryFirstPost;
import com.akatsuki.pioms.category.vo.ResponseCategoryFirstUpdate;
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

        categoryFirst.setCategory_first_name(request.getCategory_first_name());
        categoryFirst.setCategory_first_update_date(formattedDateTime);

        ResponseCategoryFirstUpdate responseValue = new ResponseCategoryFirstUpdate(updatedCategoryFirst.getCategory_first_code(), updatedCategoryFirst.getCategory_first_name(), updatedCategoryFirst.getCategory_first_update_date());
        return responseValue;
    }

    @Override
    @Transactional
    public ResponseCategoryFirstPost postCategoryFirst(RequestCategoryFirstPost request) {
        CategoryFirst categoryFirst = new CategoryFirst();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);


        categoryFirst.setCategory_first_name(request.getCategory_first_name());
        categoryFirst.setCategory_first_enroll_date(formattedDateTime);

        CategoryFirst savedCategoryFirst = categoryFirstRepository.save(categoryFirst);

        ResponseCategoryFirstPost responseValue = new ResponseCategoryFirstPost(savedCategoryFirst.getCategory_first_code(),savedCategoryFirst.getCategory_first_name(), savedCategoryFirst.getCategory_first_enroll_date());
        return responseValue;
    }
}
