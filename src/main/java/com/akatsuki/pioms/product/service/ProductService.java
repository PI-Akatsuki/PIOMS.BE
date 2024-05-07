package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProduct();

    Optional<Product> findProductByCode(int productCode);
}
