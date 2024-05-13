package com.akatsuki.pioms.categorySecond.service;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categorySecond.aggregate.*;
import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
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

    LogService logService;

    @Autowired
    public CategorySecondServiceImpl(CategorySecondRepository categorySecondRepository,LogService logService) {
        this.categorySecondRepository = categorySecondRepository;
        this.logService = logService;
    }

    @Override
    public List<CategorySecond> getAllCategorySecond() {
        return categorySecondRepository.findAll();
    }

    @Override
    public Optional<CategorySecond> findCategorySecondByCode(int categorySecondCode) {
        return categorySecondRepository.findById(categorySecondCode);
    }

    @Override
    @Transactional
    public ResponseCategorySecondPost postCategorySecond(RequestCategorySecondPost request/*, int requesterAdminCode*/) {
//        Optional<Admin> requestorAdmin = adminRepository.findById(requesterAdminCode);
//        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
//            return ResponseEntity.status(403).body("신규 카테고리 등록은 루트 관리자만 가능합니다.");
//        }
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
        logService.saveLog("root", LogStatus.등록,savedCategorySecond.getCategorySecondName(),"CategorySecond");
        return responseValue;
    }

    @Override
    @Transactional
    public ResponseCategorySecondUpdate updateCategorySecond(int categorySecondCode, RequestCategorySecondUpdate request/*, int requesterAdminCode*/) {
//        Optional<Admin> requestorAdmin = adminRepository.findById(requesterAdminCode);
//        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
//            return ResponseEntity.status(403).body("신규 카테고리 등록은 루트 관리자만 가능합니다.");
//        }
        CategorySecond categorySecond = categorySecondRepository.findById(categorySecondCode)
                .orElseThrow(() -> new EntityNotFoundException("CategorySecond not found"));

        CategorySecond updatedCategorySecond = categorySecondRepository.save(categorySecond);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        categorySecond.setCategorySecondName(request.getCategorySecondName());
        categorySecond.setCategorySecondUpdateDate(formattedDateTime);

        ResponseCategorySecondUpdate responseValue = new ResponseCategorySecondUpdate(updatedCategorySecond.getCategorySecondCode(), updatedCategorySecond.getCategorySecondName(), updatedCategorySecond.getCategorySecondUpdateDate());
        logService.saveLog("root", LogStatus.수정,updatedCategorySecond.getCategorySecondName(),"CategorySecond");
        return responseValue;
    }
}
