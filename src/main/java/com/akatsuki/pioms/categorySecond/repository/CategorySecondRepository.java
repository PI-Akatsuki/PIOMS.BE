package com.akatsuki.pioms.categorySecond.repository;

import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorySecondRepository extends JpaRepository<CategorySecond, Integer> {

    List<CategorySecond> findByCategorySecondCode(int categorySecondCode);

    List<CategorySecond> findAllByCategoryFirstCategoryFirstCode(int categoryFirstCode);
}
