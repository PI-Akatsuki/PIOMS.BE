package com.akatsuki.pioms.product.repository;

import com.akatsuki.pioms.product.entity.Product;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
