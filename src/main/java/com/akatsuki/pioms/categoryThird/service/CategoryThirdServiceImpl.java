package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThird;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdCreateDTO;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdUpdateDTO;
import com.akatsuki.pioms.categoryThird.repository.CategoryThirdRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.repository.ProductRepository;
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
public class CategoryThirdServiceImpl implements CategoryThirdService{

    private final CategoryThirdRepository categoryThirdRepository;
    private final CategorySecondRepository categorySecondRepository;
    private final ProductRepository productRepository;
    private final AdminRepository adminRepository;
    private final LogService logService;

    @Autowired
    public CategoryThirdServiceImpl(CategoryThirdRepository categoryThirdRepository, CategorySecondRepository categorySecondRepository, ProductRepository productRepository, AdminRepository adminRepository, LogService logService) {
        this.categoryThirdRepository = categoryThirdRepository;
        this.categorySecondRepository = categorySecondRepository;
        this.productRepository = productRepository;
        this.adminRepository = adminRepository;
        this.logService = logService;
    }

    @Override
    @Transactional
    public List<CategoryThirdDTO> getAllCategoryThird() {
        List<CategoryThird> categoryThirdList = categoryThirdRepository.findAll();
        List<CategoryThirdDTO> responseCategory = new ArrayList<>();

        categoryThirdList.forEach(categoryThird -> {
            responseCategory.add(new CategoryThirdDTO(categoryThird));
        });
        return responseCategory;
    }

    @Override
    @Transactional
    public List<CategoryThirdDTO> findCategoryThirdByCode(int categoryThirdCode) {
        List<CategoryThird> categoryThirdList = categoryThirdRepository.findByCategoryThirdCode(categoryThirdCode);
        List<CategoryThirdDTO> categoryThirdDTOS = new ArrayList<>();
        categoryThirdList.forEach(categoryThird -> {
            categoryThirdDTOS.add(new CategoryThirdDTO(categoryThird));
        });
        return categoryThirdDTOS;
    }

    @Override
    @Transactional
    public ResponseEntity<String> postCategory(RequestCategoryThird request) {
        CategoryThird categoryThird = new CategoryThird();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        List<CategorySecond> categorySecondList = categorySecondRepository.findByCategorySecondCode(request.getCategorySecondCode());

        if(categorySecondList == null) {
            return ResponseEntity.badRequest().body("해당 카테고리(중)이 존재하지 않습니다. 다시 확인해주세요.");
        }

        CategorySecond categorySecond = new CategorySecond();
        categorySecond.setCategorySecondCode(request.getCategorySecondCode());
        categoryThird.setCategorySecond(categorySecond);

        categoryThird.setCategoryThirdName(request.getCategoryThirdName());
        categoryThird.setCategoryThirdEnrollDate(formattedDateTime);
        categoryThird.setCategoryThirdUpdateDate(formattedDateTime);

        CategoryThird savedCategoryThird = categoryThirdRepository.save(categoryThird);
        System.out.println("savedCategoryThird = " + savedCategoryThird);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.등록, savedCategoryThird.getCategoryThirdName(), "CategoryThird");

        return ResponseEntity.ok("카테고리(소) 생성 완료!");
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateCategory(int categoryThirdCode, RequestCategoryThird request) {
        CategoryThird categoryThird = categoryThirdRepository.findById(categoryThirdCode)
                .orElseThrow(() -> new EntityNotFoundException("CategoryThird not found"));


        CategoryThird updatedCategoryThird = categoryThirdRepository.save(categoryThird);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        categoryThird.setCategoryThirdName(request.getCategoryThirdName());
        categoryThird.setCategoryThirdUpdateDate(formattedDateTime);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.수정,updatedCategoryThird.getCategoryThirdName(),"CategoryThird");
        return ResponseEntity.ok("카테고리(소) 수정 완료!");
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteCategoryThird(int categoryThirdCode) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        CategoryThird categoryThird = categoryThirdRepository.findById(categoryThirdCode)
                .orElseThrow(() -> new EntityNotFoundException("그런거 없다."));
        if (categoryThird == null) {
            return ResponseEntity.badRequest().body(categoryThirdCode + "번 카테고리(소) 카테고리가 없습니다!");
        }

        List<Product> products = productRepository.findByCategoryThird_CategoryThirdCode(categoryThirdCode);
        if (!products.isEmpty()) {
            return ResponseEntity.badRequest().body("상품이 존재하는 해당 " + categoryThirdCode + "번 카테고리(소) 카테고리는 삭제할 수 없습니다!");
        }

        categoryThird.setCategoryThirdDeleteDate(formattedDateTime);
        categoryThirdRepository.delete(categoryThird);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.삭제,categoryThird.getCategoryThirdName(),"CategoryThird");
        return ResponseEntity.badRequest().body("카테고리(소) 삭제 완료");
    }

    @Override
    public List<ResponseCategoryThird> getCategoryThirdInSecond(int categorySecondCode) {
        List<CategoryThird> categoryThirdList = categoryThirdRepository.findAllByCategorySecondCategorySecondCode(categorySecondCode);
        List<ResponseCategoryThird> responseCategoryThirds = new ArrayList<>();
        categoryThirdList.forEach(categoryThird -> {
            responseCategoryThirds.add(new ResponseCategoryThird(categoryThird));
        });
        return responseCategoryThirds;
    }

    @Override
    public CategoryThirdDTO createCategoryThird(CategoryThirdCreateDTO categoryThirdCreateDTO) {
        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setCategoryThirdName(categoryThirdCreateDTO.getCategoryThirdName());
        categoryThirdRepository.save(categoryThird);
        logService.saveLog("root", LogStatus.등록, categoryThird.getCategoryThirdName(), "CategoryThird");
        return new CategoryThirdDTO(categoryThird);
    }

    @Override
    public CategoryThird modifyCategoryThird(int categoryThirdCode, CategoryThirdUpdateDTO categoryThirdUpdateDTO) {
        CategoryThird categoryThird = categoryThirdRepository.findById(categoryThirdCode)
                .orElseThrow(() -> new EntityNotFoundException("CategoryThird not found with id: " + categoryThirdCode));

        categoryThird.setCategoryThirdName(categoryThirdUpdateDTO.getCategoryThirdName());
        categoryThirdRepository.save(categoryThird);
        logService.saveLog("root", LogStatus.수정,categoryThird.getCategoryThirdName(),"CategoryThird");
        return categoryThirdRepository.save(categoryThird);
    }

}
