package com.akatsuki.pioms.product.repository;

import com.akatsuki.pioms.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
}
