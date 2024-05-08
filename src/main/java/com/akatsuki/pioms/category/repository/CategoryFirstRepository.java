package com.akatsuki.pioms.category.repository;

import com.akatsuki.pioms.category.entity.CategoryFirst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryFirstRepository extends JpaRepository<CategoryFirst,Integer> {

}
