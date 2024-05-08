package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.entity.ProductEntity;
import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.vo.RequestProductPost;
import com.akatsuki.pioms.product.vo.ResponseProductPost;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProduct();
    ProductEntity getProduct(int productId);
    Optional<Product> findProductByCode(int productCode);
    ResponseProductPost postProduct(RequestProductPost request);
}
