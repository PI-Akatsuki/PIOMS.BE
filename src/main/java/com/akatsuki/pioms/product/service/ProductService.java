package com.akatsuki.pioms.product.service;


import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.order.entity.Order;
import com.akatsuki.pioms.order.entity.OrderProduct;
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
    Product findProductByCode(int productCode);
    String postProduct(RequestProduct request/*, int requesterAdminCode*/);

    String deleteProduct(int productCode/*, int requesterAdminCode*/);
    ResponseProduct updateProduct(int productCode, RequestProduct request/*, int requesterAdminCode*/);
    void exportProducts(Order order);
    void exportExchangeProducts(int exchange);
    boolean checkExchangeProduct(Order order, ExchangeDTO exchange);
    List<ResponseProducts> getCategoryProductList(int categoryThirdCode);

    void editUncorrectCount(Product product, int i);
}
