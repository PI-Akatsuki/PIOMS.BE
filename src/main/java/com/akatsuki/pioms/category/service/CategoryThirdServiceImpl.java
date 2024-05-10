package com.akatsuki.pioms.category.service;

import com.akatsuki.pioms.admin.entity.Admin;
import com.akatsuki.pioms.category.dto.CategoryThirdDTO;
import com.akatsuki.pioms.category.entity.CategorySecond;
import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.category.repository.CategoryThirdRepository;
import com.akatsuki.pioms.category.vo.RequestCategoryPost;
import com.akatsuki.pioms.category.vo.RequestCategoryUpdate;
import com.akatsuki.pioms.category.vo.ResponseCategoryPost;
import com.akatsuki.pioms.product.controller.ProductController;
import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MatchingStrategy;
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
    public ResponseCategoryPost postCategory(RequestCategoryPost request) {
        CategoryThird categoryThird = new CategoryThird();

        // CategorySecond 엔티티를 참조하는 필드에 해당 CategorySecond 엔티티를 설정
        CategorySecond categorySecond = new CategorySecond();
        categorySecond.setCategory_second_code(request.getCategory_second_code());
        categoryThird.setCategory_second_code(categorySecond);

        categoryThird.setCategory_third_name(request.getCategory_third_name());

        CategoryThird savedCategoryThird = categoryThirdRepository.save(categoryThird);

        ResponseCategoryPost responseValue = new ResponseCategoryPost(savedCategoryThird.getCategory_third_code(), savedCategoryThird.getCategory_third_name());
        return responseValue;
    }

    /* 카테고리(소) 수정 */
    @Override
    @Transactional
    public ResponseCategoryPost updateCategory(int categoryThirdCode, RequestCategoryUpdate request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        admin.setDeleteDate(formattedDateTime);

        CategoryThird categoryThird = categoryThirdRepository.findById(categoryThirdCode)
                .orElseThrow(() -> new EntityNotFoundException("CategoryThird not found"));

        categoryThird.setCategory_third_name(request.getCategory_third_name());

        CategoryThird updatedCategoryThird = categoryThirdRepository.save(categoryThird);

        ResponseCategoryPost responseValue = new ResponseCategoryPost(updatedCategoryThird.getCategory_third_code(), updatedCategoryThird.getCategory_third_name());
        return responseValue;
    }


//    @Override
//    @Transactional
//    public CategoryThird deleteCategory(int categoryThirdCode) {
//        System.out.println("serviceImpl 왔음.");
//        CategoryThird categoryThird = categoryThirdRepository.findById(categoryThirdCode)
//                .orElseThrow(() -> new EntityNotFoundException("category_third_code가 " + categoryThirdCode + "인 카테고리(소)가 없음."));
//
//        return null;
//        Optional<Product> products = productRepository.findById(categoryThirdCode);
//        if(products.isEmpty()) {
//            categoryThirdRepository.deleteById(categoryThirdCode);
//            return null;
//        }
//    }
}
