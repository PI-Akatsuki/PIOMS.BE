package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThirdUpdate;
import com.akatsuki.pioms.categoryThird.repository.CategoryThirdRepository;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThirdPost;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThirdPost;
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

    @Autowired
    public CategoryThirdServiceImpl(CategoryThirdRepository categoryThirdRepository) {
        this.categoryThirdRepository = categoryThirdRepository;
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
    public ResponseCategoryThirdPost postCategory(RequestCategoryThirdPost request) {
        CategoryThird categoryThird = new CategoryThird();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        // CategorySecond 엔티티를 참조하는 필드에 해당 CategorySecond 엔티티를 설정
        CategorySecond categorySecond = new CategorySecond();
        categorySecond.setCategory_second_code(request.getCategory_second_code());
        categoryThird.setCategory_second_code(categorySecond);

        categoryThird.setCategory_third_name(request.getCategory_third_name());
        categoryThird.setCategory_third_enroll_date(formattedDateTime);

        CategoryThird savedCategoryThird = categoryThirdRepository.save(categoryThird);

        ResponseCategoryThirdPost responseValue = new ResponseCategoryThirdPost(savedCategoryThird.getCategory_third_code(), savedCategoryThird.getCategory_third_name(), savedCategoryThird.getCategory_third_enroll_date());
        return responseValue;
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

        categoryThird.setCategory_third_name(request.getCategory_third_name());
        categoryThird.setCategory_third_update_date(formattedDateTime);

        ResponseCategoryThirdPost responseValue = new ResponseCategoryThirdPost(updatedCategoryThird.getCategory_third_code(), updatedCategoryThird.getCategory_third_name(), updatedCategoryThird.getCategory_third_update_date());
        return responseValue;
    }

//    @Override
//    @Transactional
//    public CategoryThird deleteCategory(int categoryThirdCode) {
//        categoryThirdRepository.deleteById(categoryThirdCode);
//        return null;
//    }
}
