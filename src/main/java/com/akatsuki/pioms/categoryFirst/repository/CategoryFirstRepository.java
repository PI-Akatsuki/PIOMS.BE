package com.akatsuki.pioms.categoryFirst.repository;

import com.akatsuki.pioms.categoryFirst.aggregate.CategoryFirst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryFirstRepository extends JpaRepository<CategoryFirst,Integer> {

}
