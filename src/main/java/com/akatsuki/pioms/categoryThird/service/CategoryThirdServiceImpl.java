package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import com.akatsuki.pioms.categorySecond.repository.CategorySecondRepository;
import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThirdUpdate;
import com.akatsuki.pioms.categoryThird.repository.CategoryThirdRepository;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThirdPost;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThirdPost;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryThirdServiceImpl implements CategoryThirdService{

    private final CategoryThirdRepository categoryThirdRepository;
    private final CategorySecondRepository categorySecondRepository;
    private final ProductRepository productRepository;
    LogService logService;

    @Autowired
    public CategoryThirdServiceImpl(CategoryThirdRepository categoryThirdRepository, CategorySecondRepository categorySecondRepository, ProductRepository productRepository, LogService logService) {
        this.categoryThirdRepository = categoryThirdRepository;
        this.categorySecondRepository = categorySecondRepository;
        this.productRepository = productRepository;
        this.logService = logService;
    }

    /* 카테고리(소) 전체 조회 */
    @Override
    public List<CategoryThird> getAllCategoryThird() {
        return categoryThirdRepository.findAll();
    }

    /* 카테고리(소) 코드로 카테고리(소) 조회 */
    @Override
    public Optional<CategoryThird> findCategoryThirdByCode(int categoryThirdCode) {
        return categoryThirdRepository.findById(categoryThirdCode);
    }

    /* 카테고리(소) 신규 등록 */
    @Override
    @Transactional
    public String postCategory(RequestCategoryThirdPost request) {

        // CategoryThird 객체 생성
        CategoryThird categoryThird = new CategoryThird();

        // 현재 날짜 및 시간 포맷 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        // request에서 받은 categorySecondCode를 사용하여 CategorySecond 객체 검색
        CategorySecond categorySecond = categorySecondRepository.findByCategorySecondCode(request.getCategorySecondCode());

        // 만약 해당 CategorySecond가 존재하지 않으면 메시지 반환
        if(categorySecond == null) {
            return "해당 카테고리(중)이 존재하지 않습니다. 다시 확인해주세요.";
        }

        // CategorySecond 객체를 CategoryThird 객체에 설정
        categoryThird.setCategorySecondCode(categorySecond);

        // 요청으로부터 받은 정보를 CategoryThird 객체에 설정
        categoryThird.setCategoryThirdName(request.getCategoryThirdName());
        categoryThird.setCategoryThirdEnrollDate(formattedDateTime);

        // CategoryThird 저장
        CategoryThird savedCategoryThird = categoryThirdRepository.save(categoryThird);

        // 로그 저장
        logService.saveLog("root", LogStatus.등록, savedCategoryThird.getCategoryThirdName(), "CategoryThird");

        // 성공 메시지 반환
        return "카테고리(소) 생성 완료!";
    }

    /* 카테고리(소) 수정 */
    @Override
    @Transactional
    public ResponseCategoryThirdPost updateCategory(int categoryThirdCode, RequestCategoryThirdUpdate request) {

        CategoryThird categoryThird = categoryThirdRepository.findById(categoryThirdCode)
                .orElseThrow(() -> new EntityNotFoundException("CategoryThird not found"));


        CategoryThird updatedCategoryThird = categoryThirdRepository.save(categoryThird);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        categoryThird.setCategoryThirdName(request.getCategoryThirdName());
        categoryThird.setCategoryThirdUpdateDate(formattedDateTime);

        ResponseCategoryThirdPost responseValue = new ResponseCategoryThirdPost(updatedCategoryThird.getCategoryThirdCode(), updatedCategoryThird.getCategoryThirdName(), updatedCategoryThird.getCategoryThirdUpdateDate());
        logService.saveLog("root", LogStatus.수정,updatedCategoryThird.getCategoryThirdName(),"CategoryThird");
        return responseValue;
    }

    @Override
    @Transactional
    public String deleteCategoryThird(int categoryThirdCode) {
        CategoryThird categoryThird = categoryThirdRepository.findByCategoryThirdCode(categoryThirdCode);
        if (categoryThird == null) {
            return categoryThirdCode + "번 카테고리(소) 카테고리가 없습니다!";
        }

        // Check if there are any products associated with this categoryThirdCode
        List<Product> products = productRepository.findByCategoryThird_CategoryThirdCode(categoryThirdCode);
        if (!products.isEmpty()) {
            return "상품이 존재하는 해당 " + categoryThirdCode + "번 카테고리(소) 카테고리는 삭제할 수 없습니다!";
        }

        categoryThirdRepository.delete(categoryThird);
        logService.saveLog("root", LogStatus.삭제,categoryThird.getCategoryThirdName(),"CategoryThird");
        return categoryThirdCode + "번의 해당 카테고리(소) 카테고리가 성공적으로 삭제되었습니다!";
    }
//    @Override
//    @Transactional
//    public CategoryThird deleteCategory(int categoryThirdCode) {
//        categoryThirdRepository.deleteById(categoryThirdCode);
//        return null;
//    }
}
