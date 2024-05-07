package com.akatsuki.pioms.category.repository;

import com.akatsuki.pioms.category.entity.CategoryThird;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryThirdDAO extends JpaRepository<CategoryThird, Integer> {
}
