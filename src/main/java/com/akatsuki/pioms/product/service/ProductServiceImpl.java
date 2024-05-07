package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.entity.ProductEntity;
import com.akatsuki.pioms.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity getProduct(int productId){
        return productRepository.findById(productId).orElseThrow();
    }
}
