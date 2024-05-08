package com.akatsuki.pioms.product.repository;

import com.akatsuki.pioms.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
}
