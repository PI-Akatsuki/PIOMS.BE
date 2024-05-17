package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThirdPost;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThirdUpdate;
import com.akatsuki.pioms.categoryThird.dto.CategoryThirdDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryThirdService {
    List<CategoryThirdDTO> getAllCategoryThird();

    List<CategoryThirdDTO> findCategoryThirdByCode(int categoryThirdCode);

    ResponseEntity<String> postCategory(RequestCategoryThirdPost request, int requesterAdminCode);

    ResponseEntity<String> updateCategory(int categoryThirdCode, RequestCategoryThirdUpdate request, int requesterAdminCode);

    ResponseEntity<String> deleteCategoryThird(int categoryThirdCode, int requesterAdminCode);

}
