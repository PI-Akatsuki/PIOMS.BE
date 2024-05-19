package com.akatsuki.pioms.categoryThird.repository;

import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryThirdRepository extends JpaRepository<CategoryThird, Integer> {
    List<CategoryThird> findByCategoryThirdCode(int categoryThirdCode);

    List<CategoryThird> findAllByCategorySecondCategorySecondCode(int categorySecondCode);
}
