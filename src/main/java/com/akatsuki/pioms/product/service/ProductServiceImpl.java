package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.repository.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductDAO productDAO;

    @Autowired
    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    @Override
    public List<Product> getAllProduct() {
        return productDAO.findAll();
    }

    @Override
    public Optional<Product> findProductByCode(int productCode) {
        return productDAO.findById(productCode);
    }
}
