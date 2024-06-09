package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstCreateDTO;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstUpdateDTO;
import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirst;
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
public class CategoryFirstServiceImpl implements CategoryFirstService {
    private final CategoryFirstRepository categoryFirstRepository;
    private final AdminRepository adminRepository;
    private final LogService logService;

    @Autowired
    public CategoryFirstServiceImpl(CategoryFirstRepository categoryFirstRepository, AdminRepository adminRepository, LogService logService) {
        this.categoryFirstRepository = categoryFirstRepository;
        this.adminRepository = adminRepository;
        this.logService =  logService;
    }

    @Override
    @Transactional
    public List<CategoryFirstDTO> getAllCategoryFirst() {
        List<CategoryFirst> categoryFirstList = categoryFirstRepository.findAll();
        List<CategoryFirstDTO> responseCategory = new ArrayList<>();

        categoryFirstList.forEach(categoryFirst -> {
            responseCategory.add(new CategoryFirstDTO(categoryFirst));
        });
        return responseCategory;
//        return categoryFirstRepository.findAll();
    }

    @Override
    @Transactional
    public List<CategoryFirstDTO> findCategoryFirstByCode(int categoryFirstCode) {
        List<CategoryFirst> categoryFirstList = categoryFirstRepository.findByCategoryFirstCode(categoryFirstCode);
        List<CategoryFirstDTO> categoryFirstDTOS = new ArrayList<>();
        categoryFirstList.forEach(categoryFirst -> {
            categoryFirstDTOS.add(new CategoryFirstDTO(categoryFirst));
        });
        return categoryFirstDTOS;
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateCategoryFirst(int categoryFirstCode, RequestCategoryFirst request) {

        CategoryFirst categoryFirst = categoryFirstRepository.findById(categoryFirstCode)
                .orElseThrow(() -> new EntityNotFoundException("CategoryFirst not found"));

        CategoryFirst updatedCategoryFirst = categoryFirstRepository.save(categoryFirst);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        categoryFirst.setCategoryFirstName(request.getCategoryFirstName());
        categoryFirst.setCategoryFirstUpdateDate(formattedDateTime);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.수정,updatedCategoryFirst.getCategoryFirstName(),"CategoryFirst");
        return ResponseEntity.ok("카테고리(대) 수정 완료!");
    }

    @Override
    @Transactional
    public ResponseEntity<String> postCategoryFirst(RequestCategoryFirst request) {
        CategoryFirst categoryFirst = new CategoryFirst();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);


        categoryFirst.setCategoryFirstName(request.getCategoryFirstName());
        categoryFirst.setCategoryFirstEnrollDate(formattedDateTime);
        categoryFirst.setCategoryFirstUpdateDate(formattedDateTime);

        CategoryFirst savedCategoryFirst = categoryFirstRepository.save(categoryFirst);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.등록,savedCategoryFirst.getCategoryFirstName(),"CategoryFirst");
        return ResponseEntity.ok("카테고리(대) 생성 완료!");
    }

    @Override
    public ResponseEntity<String> deleteCategoryFirst(int categoryFirstCode) {
        CategoryFirst categoryFirst = categoryFirstRepository.findById(categoryFirstCode)
                .orElseThrow(() -> new EntityNotFoundException("CategoryFirst not found"));
        if(categoryFirst == null) {
            return ResponseEntity.badRequest().body(categoryFirstCode + "번 카테고리 없음");
        }

        categoryFirstRepository.delete(categoryFirst);
        logService.saveLog("root", LogStatus.삭제,categoryFirst.getCategoryFirstName(),"CategoryFirst");
        return ResponseEntity.badRequest().body("카테고리 소분류 삭제 완료");
    }

    @Override
    public CategoryFirstDTO createCategoryFirst(CategoryFirstCreateDTO categoryFirstCreateDTO) {
        CategoryFirst categoryFirst = new CategoryFirst();
        categoryFirst.setCategoryFirstName(categoryFirstCreateDTO.getCategoryFirstName());
        categoryFirstRepository.save(categoryFirst);
        logService.saveLog("root", LogStatus.등록,categoryFirst.getCategoryFirstName(),"CategoryFirst");
        return new CategoryFirstDTO(categoryFirst);
    }

    @Override
    public CategoryFirst modifyCategoryFirst(int categoryFirstCode, CategoryFirstUpdateDTO categoryFirstUpdateDTO) {
        CategoryFirst categoryFirst = categoryFirstRepository.findById(categoryFirstCode).
                orElseThrow(() -> new EntityNotFoundException("CategoryFirst not found with id: " + categoryFirstCode));

        categoryFirst.setCategoryFirstName(categoryFirstUpdateDTO.getCategoryFirstName());
        categoryFirstRepository.save(categoryFirst);

        logService.saveLog("root", LogStatus.수정,categoryFirst.getCategoryFirstName(),"CategoryFirst");
        return categoryFirstRepository.save(categoryFirst);
    }
}
