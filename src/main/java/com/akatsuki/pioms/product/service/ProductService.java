package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProducts;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    List<Product> getAllProduct();
    Product getProduct(int productId);
    Optional<Product> findProductByCode(int productCode);
    ResponseProduct postProduct(RequestProduct request);

    Product deleteProduct(int productCode);

    ResponseProduct updateProduct(int productCode, RequestProduct request);

    List<ResponseProducts> getCategoryProductList(int categoryThirdCode);
}
