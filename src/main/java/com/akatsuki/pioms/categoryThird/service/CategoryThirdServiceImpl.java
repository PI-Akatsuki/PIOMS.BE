package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThird;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import com.akatsuki.pioms.categoryThird.repository.CategoryThirdRepository;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.repository.ProductRepository;
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
    public ResponseEntity<String> postCategory(RequestCategoryThird request/*, int requesterAdminCode*/) {
//        Optional<Admin> requestorAdmin = adminRepository.findById(requesterAdminCode);
//        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
//            return ResponseEntity.status(403).body("신규 카테고리 등록은 루트 관리자만 가능합니다.");
//        }

        CategoryThird categoryThird = new CategoryThird();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        List<CategorySecond> categorySecond = categorySecondRepository.findByCategorySecondCode(request.getCategorySecondCode());

        if(categorySecond == null) {
            return ResponseEntity.badRequest().body("해당 카테고리(중)이 존재하지 않습니다. 다시 확인해주세요.");
        }

        categoryThird.setCategorySecondCode(request.getCategorySecondCode());

        categoryThird.setCategoryThirdName(request.getCategoryThirdName());
        categoryThird.setCategoryThirdEnrollDate(formattedDateTime);

        CategoryThird savedCategoryThird = categoryThirdRepository.save(categoryThird);
        System.out.println("savedCategoryThird = " + savedCategoryThird);
        logService.saveLog("root", LogStatus.등록, savedCategoryThird.getCategoryThirdName(), "CategoryThird");

        return ResponseEntity.ok("카테고리(소) 생성 완료!");
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateCategory(int categoryThirdCode, RequestCategoryThird request, int requesterAdminCode) {
        Optional<Admin> requestorAdmin = adminRepository.findById(requesterAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("카테고리 수정은 루트 관리자만 가능합니다.");
        }
        CategoryThird categoryThird = categoryThirdRepository.findById(categoryThirdCode)
                .orElseThrow(() -> new EntityNotFoundException("CategoryThird not found"));


        CategoryThird updatedCategoryThird = categoryThirdRepository.save(categoryThird);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        categoryThird.setCategoryThirdName(request.getCategoryThirdName());
        categoryThird.setCategoryThirdUpdateDate(formattedDateTime);

        logService.saveLog("root", LogStatus.수정,updatedCategoryThird.getCategoryThirdName(),"CategoryThird");
        return ResponseEntity.ok("카테고리(소) 수정 완료!");
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteCategoryThird(int categoryThirdCode, int requesterAdminCode) {
        Optional<Admin> requestorAdmin = adminRepository.findById(requesterAdminCode);
        if (requestorAdmin.isEmpty() || requestorAdmin.get().getAdminCode() != 1) {
            return ResponseEntity.status(403).body("카테고리 삭제는 루트 관리자만 가능합니다.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        CategoryThird categoryThird = categoryThirdRepository.findById(categoryThirdCode)
                .orElseThrow(() -> new EntityNotFoundException("그런거 없다."));
        if (categoryThird == null) {
            return ResponseEntity.badRequest().body(categoryThirdCode + "번 카테고리(소) 카테고리가 없습니다!");
        }

        // Check if there are any products associated with this categoryThirdCode
        List<Product> products = productRepository.findByCategoryThird_CategoryThirdCode(categoryThirdCode);
        if (!products.isEmpty()) {
            return ResponseEntity.badRequest().body("상품이 존재하는 해당 " + categoryThirdCode + "번 카테고리(소) 카테고리는 삭제할 수 없습니다!");
        }

        categoryThird.setCategoryThirdDeleteDate(formattedDateTime);
        categoryThirdRepository.delete(categoryThird);
        logService.saveLog("root", LogStatus.삭제,categoryThird.getCategoryThirdName(),"CategoryThird");
        return ResponseEntity.badRequest().body(categoryThirdCode + "번의 해당 카테고리(소) 카테고리가 성공적으로 삭제되었습니다!");
    }

}
