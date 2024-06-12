package com.akatsuki.pioms.categorySecond.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
import com.akatsuki.pioms.categorySecond.aggregate.*;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondCreateDTO;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondDTO;
import com.akatsuki.pioms.categorySecond.dto.CategorySecondUpdateDTO;
import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategorySecondServiceImpl implements CategorySecondService{

    private final CategorySecondRepository categorySecondRepository;
    private final CategoryFirstRepository categoryFirstRepository;
    private final AdminRepository adminRepository;

    private final LogService logService;

    @Autowired
    public CategorySecondServiceImpl(CategorySecondRepository categorySecondRepository, CategoryFirstRepository categoryFirstRepository, AdminRepository adminRepository, LogService logService) {
        this.categorySecondRepository = categorySecondRepository;
        this.categoryFirstRepository = categoryFirstRepository;
        this.adminRepository = adminRepository;
        this.logService = logService;
    }

    @Override
    @Transactional
    public List<CategorySecondDTO> getAllCategorySecond() {
        List<CategorySecond> categorySecondList = categorySecondRepository.findAll();
        List<CategorySecondDTO> responseCategory = new ArrayList<>();

        categorySecondList.forEach(categorySecond -> {
            responseCategory.add(new CategorySecondDTO(categorySecond));
        });
        return responseCategory;
    }

    @Override
    @Transactional
    public List<CategorySecondDTO> findCategorySecondByCode(int categorySecondCode) {
        List<CategorySecond> categorySecondList = categorySecondRepository.findByCategorySecondCode(categorySecondCode);
        List<CategorySecondDTO> categorySecondDTOS = new ArrayList<>();
        categorySecondList.forEach(categorySecond -> {
            categorySecondDTOS.add(new CategorySecondDTO(categorySecond));
        });
        return categorySecondDTOS;
    }

    @Override
    @Transactional
    public ResponseEntity<String> postCategorySecond(RequestCategorySecond request) {

        CategorySecond categorySecond = new CategorySecond();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        List<CategoryFirst> categoryFirstList = categoryFirstRepository.findByCategoryFirstCode(request.getCategoryFirstCode());

        CategoryFirst categoryFirst = new CategoryFirst();
        categoryFirst.setCategoryFirstCode(request.getCategoryFirstCode());
        categorySecond.setCategoryFirst(categoryFirst);

        categorySecond.setCategorySecondName(request.getCategorySecondName());
        categorySecond.setCategorySecondEnrollDate(formattedDateTime);
        categorySecond.setCategorySecondUpdateDate(formattedDateTime);

        CategorySecond savedCategorySecond = categorySecondRepository.save(categorySecond);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.등록,savedCategorySecond.getCategorySecondName(),"CategorySecond");
        return ResponseEntity.ok("카테고리(중) 생성 완료!");
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateCategorySecond(int categorySecondCode, RequestCategorySecond request) {

        CategorySecond categorySecond = categorySecondRepository.findById(categorySecondCode)
                .orElseThrow(() -> new EntityNotFoundException("CategorySecond not found"));

        CategorySecond updatedCategorySecond = categorySecondRepository.save(categorySecond);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        categorySecond.setCategorySecondName(request.getCategorySecondName());
        categorySecond.setCategorySecondUpdateDate(formattedDateTime);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.수정,updatedCategorySecond.getCategorySecondName(),"CategorySecond");
        return ResponseEntity.ok("카테고리(중) 수정 완료!");
    }

    @Override
    @Transactional
    public List<ResponseCategorySecond> getCategorySecondInFirst(int categoryFirstCode) {
        List<CategorySecond> categorySecondList = categorySecondRepository.findAllByCategoryFirstCategoryFirstCode(categoryFirstCode);
        List<ResponseCategorySecond> responseCategorySeconds = new ArrayList<>();
        categorySecondList.forEach(categorySecond -> {
            responseCategorySeconds.add(new ResponseCategorySecond(categorySecond));
        });
        return responseCategorySeconds;
    }

    @Override
    public ResponseEntity<String> deleteCategorySecond(int categorySecondCode) {
        CategorySecond categorySecond = categorySecondRepository.findById(categorySecondCode)
                .orElseThrow(() -> new EntityNotFoundException("NoNo"));
        if (categorySecond == null) {
            return ResponseEntity.badRequest().body(categorySecondCode + "번 카테고리(소) 카테고리가 없습니다!");
        }

        categorySecondRepository.delete(categorySecond);
        logService.saveLog("root", LogStatus.삭제,categorySecond.getCategorySecondName(),"CategorySecond");
        return ResponseEntity.badRequest().body("카테고리(중) 삭제 완료");
    }

    @Override
    public CategorySecondDTO createCategorySecond(CategorySecondCreateDTO categorySecondCreateDTO) {
        CategorySecond categorySecond = new CategorySecond();
        categorySecond.setCategorySecondName(categorySecondCreateDTO.getCategorySecondName());
        categorySecondRepository.save(categorySecond);
        logService.saveLog("root", LogStatus.등록,categorySecond.getCategorySecondName(),"CategorySecond");
        return new CategorySecondDTO(categorySecond);
    }

    @Override
    public CategorySecond modifyCategorySecond(int categorySecondCode, CategorySecondUpdateDTO categorySecondUpdateDTO) {
        CategorySecond categorySecond = categorySecondRepository.findById(categorySecondCode).
                orElseThrow(() -> new EntityNotFoundException("CategorySecond not found with id:" + categorySecondCode));

        categorySecond.setCategorySecondName(categorySecondUpdateDTO.getCategorySecondName());
        categorySecondRepository.save(categorySecond);

        logService.saveLog("root", LogStatus.수정,categorySecond.getCategorySecondName(),"CategorySecond");
        return categorySecondRepository.save(categorySecond);
    }
}
