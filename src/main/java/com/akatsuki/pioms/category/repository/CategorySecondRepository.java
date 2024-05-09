package com.akatsuki.pioms.category.repository;

import com.akatsuki.pioms.category.entity.CategorySecond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorySecondRepository extends JpaRepository<CategorySecond, Integer> {

}
