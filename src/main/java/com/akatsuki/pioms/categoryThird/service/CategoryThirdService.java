package com.akatsuki.pioms.categoryThird.service;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThirdPost;
import com.akatsuki.pioms.categoryThird.aggregate.RequestCategoryThirdUpdate;
import com.akatsuki.pioms.categoryThird.aggregate.ResponseCategoryThirdPost;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryThirdService {
    List<CategoryThird> getAllCategoryThird();

    Optional<CategoryThird> findCategoryThirdByCode(int categoryThirdCode);

    ResponseEntity<String> postCategory(RequestCategoryThirdPost request/*, int requesterAdminCode*/);

    ResponseEntity<String> updateCategory(int categoryThirdCode, RequestCategoryThirdUpdate request);

    String deleteCategoryThird(int categoryThirdCode);


//    CategoryThird deleteCategory(int categoryThirdCode);
}
