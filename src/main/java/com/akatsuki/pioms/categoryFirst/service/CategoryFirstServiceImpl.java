package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirstPost;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirstUpdate;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirstPost;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirstUpdate;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CategoryFirstServiceImpl implements CategoryFirstService {
    private final CategoryFirstRepository categoryFirstRepository;
    LogService logService;

    @Autowired
    public CategoryFirstServiceImpl(CategoryFirstRepository categoryFirstRepository,LogService logService) {
        this.categoryFirstRepository = categoryFirstRepository;
        this.logService =  logService;
    }

    @Override
    public List<CategoryFirst> getAllCategoryFirst() {
        return categoryFirstRepository.findAll();
    }

    @Override
    public CategoryFirst findCategoryFirstByCode(int categoryFirstCode) {
        return categoryFirstRepository.findById(categoryFirstCode).orElseThrow(null);
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
        logService.saveLog("root", LogStatus.수정,updatedCategoryFirst.getCategoryFirstName(),"CategoryFirst");
        return responseValue;
    }

    @Override
    @Transactional
    public ResponseEntity<String> postCategoryFirst(RequestCategoryFirstPost request) {
        CategoryFirst categoryFirst = new CategoryFirst();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);


        categoryFirst.setCategoryFirstName(request.getCategoryFirstName());
        categoryFirst.setCategoryFirstEnrollDate(formattedDateTime);

        CategoryFirst savedCategoryFirst = categoryFirstRepository.save(categoryFirst);

        ResponseCategoryFirstPost responseValue = new ResponseCategoryFirstPost(savedCategoryFirst.getCategoryFirstCode(),savedCategoryFirst.getCategoryFirstName(), savedCategoryFirst.getCategoryFirstEnrollDate());
        logService.saveLog("root", LogStatus.등록,savedCategoryFirst.getCategoryFirstName(),"CategoryFirst");
        return ResponseEntity.ok("카테고리(대) 생성 완료!");
    }
}
