package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.vo.RequestProductPost;
import com.akatsuki.pioms.product.vo.ResponseProductPost;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    List<Product> getAllProduct();
    Product getProduct(int productId);
    Optional<Product> findProductByCode(int productCode);
    ResponseProductPost postProduct(RequestProductPost request);

    Product deleteProduct(int productCode);

    ResponseProductPost updateProduct(int productCode, RequestProductPost request);

//    RequestProductPost getProductsByCategoryThirdCode(Integer categoryThirdCode);

}
