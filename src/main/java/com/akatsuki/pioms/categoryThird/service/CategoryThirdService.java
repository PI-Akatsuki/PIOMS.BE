package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThird;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdCreateDTO;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdUpdateDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryThirdService {
    List<CategoryThirdDTO> getAllCategoryThird();

    List<CategoryThirdDTO> findCategoryThirdByCode(int categoryThirdCode);

    ResponseEntity<String> postCategory(RequestCategoryThird request);

    ResponseEntity<String> updateCategory(int categoryThirdCode, RequestCategoryThird request);

    ResponseEntity<String> deleteCategoryThird(int categoryThirdCode);

    List<ResponseCategoryThird> getCategoryThirdInSecond(int categorySecondCode);

    CategoryThirdDTO createCategoryThird(CategoryThirdCreateDTO categoryThirdCreateDTO);
    CategoryThird modifyCategoryThird(int categoryThirdCode, CategoryThirdUpdateDTO categoryThirdUpdateDTO);
}
