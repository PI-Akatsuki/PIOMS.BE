package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.order.entity.Order;
import com.akatsuki.pioms.order.entity.OrderProduct;
import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.vo.RequestProductPost;
import com.akatsuki.pioms.product.vo.ResponseProductPost;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProduct();
    Product getProduct(int productId);
    Optional<Product> findProductByCode(int productCode);
    ResponseProductPost postProduct(RequestProductPost request);

    Product deleteProduct(int productCode);

    ResponseProductPost updateProduct(int productCode, RequestProductPost request);

    void exportProducts(Order order);
    void exportExchangeProducts(int exchange);

    boolean checkExchangeProduct(Order order, ExchangeDTO exchange);
}
