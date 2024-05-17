package com.akatsuki.pioms.categoryFirst.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import com.akatsuki.pioms.categoryFirst.aggregate.ResponseCategoryFirst;
import com.akatsuki.pioms.categoryFirst.dto.CategoryFirstDTO;
import com.akatsuki.pioms.categoryFirst.repository.CategoryFirstRepository;
import com.akatsuki.pioms.categoryFirst.aggregate.RequestCategoryFirst;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    LogService logService;

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
    public ResponseEntity<String> updateCategoryFirst(int categoryFirstCode, RequestCategoryFirst request, int requesterAdminCode) {
        Optional<Admin> requestorAdmin = adminRepository.findById(requesterAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("신규 카테고리 등록은 루트 관리자만 가능합니다.");
        }
        CategoryFirst categoryFirst = categoryFirstRepository.findById(categoryFirstCode)
                .orElseThrow(() -> new EntityNotFoundException("CategoryFirst not found"));

        CategoryFirst updatedCategoryFirst = categoryFirstRepository.save(categoryFirst);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        categoryFirst.setCategoryFirstName(request.getCategoryFirstName());
        categoryFirst.setCategoryFirstUpdateDate(formattedDateTime);

        ResponseCategoryFirst responseValue = new ResponseCategoryFirst(updatedCategoryFirst.getCategoryFirstCode(), updatedCategoryFirst.getCategoryFirstName(), updatedCategoryFirst.getCategoryFirstEnrollDate(), updatedCategoryFirst.getCategoryFirstUpdateDate());
        logService.saveLog("root", LogStatus.수정,updatedCategoryFirst.getCategoryFirstName(),"CategoryFirst");
        return ResponseEntity.ok("카테고리(대) 수정 완료!");
    }

    @Override
    @Transactional
    public ResponseEntity<String> postCategoryFirst(RequestCategoryFirst request, int requesterAdminCode) {
        Optional<Admin> requestorAdmin = adminRepository.findById(requesterAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("신규 카테고리 등록은 루트 관리자만 가능합니다.");
        }
        CategoryFirst categoryFirst = new CategoryFirst();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);


        categoryFirst.setCategoryFirstName(request.getCategoryFirstName());
        categoryFirst.setCategoryFirstEnrollDate(formattedDateTime);

        CategoryFirst savedCategoryFirst = categoryFirstRepository.save(categoryFirst);

        ResponseCategoryFirst responseValue = new ResponseCategoryFirst(savedCategoryFirst.getCategoryFirstCode(),savedCategoryFirst.getCategoryFirstName(), savedCategoryFirst.getCategoryFirstEnrollDate(), savedCategoryFirst.getCategoryFirstUpdateDate());
        logService.saveLog("root", LogStatus.등록,savedCategoryFirst.getCategoryFirstName(),"CategoryFirst");
        return ResponseEntity.ok("카테고리(대) 생성 완료!");
    }
}
