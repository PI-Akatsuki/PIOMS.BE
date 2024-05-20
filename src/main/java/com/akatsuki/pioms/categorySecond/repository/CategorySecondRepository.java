package com.akatsuki.pioms.categorySecond.repository;

import com.akatsuki.pioms.categorySecond.aggregate.CategorySecond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorySecondRepository extends JpaRepository<CategorySecond, Integer> {

    CategorySecond findByCategorySecondCode(int categorySecondCode);
}
