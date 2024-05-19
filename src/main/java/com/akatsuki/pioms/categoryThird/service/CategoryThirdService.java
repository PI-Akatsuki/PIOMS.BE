package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThird;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryThirdService {
    List<CategoryThirdDTO> getAllCategoryThird();

    List<CategoryThirdDTO> findCategoryThirdByCode(int categoryThirdCode);

    ResponseEntity<String> postCategory(RequestCategoryThird request/*, int requesterAdminCode*/);

    ResponseEntity<String> updateCategory(int categoryThirdCode, RequestCategoryThird request/*, int requesterAdminCode*/);

    ResponseEntity<String> deleteCategoryThird(int categoryThirdCode/*, int requesterAdminCode*/);

    List<ResponseCategoryThird> getCategoryThirdInSecond(int categorySecondCode);
}
