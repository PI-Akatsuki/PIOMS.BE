package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import com.akatsuki.pioms.product.dto.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductService {
    List<ProductDTO> getAllProduct();
    List<ProductDTO> findProductByCode(int productCode);
    ResponseEntity<String> postProduct(RequestProduct request, int requesterAdminCode);
    ResponseEntity<String> deleteProduct(int productCode, int requesterAdminCode);
    ResponseEntity<String> updateProduct(int productCode, RequestProduct request, int requesterAdminCode);
    void exportProducts(OrderDTO order);
    void exportExchangeProducts(int exchange);
    boolean checkExchangeProduct(OrderDTO order, ExchangeDTO exchange);
    List<ResponseProduct> getCategoryProductList(int categoryThirdCode);

    // 가맹점에서 발주온 상품을 검수할 때 수량 불일치 시 이를 본사 창고에 반영하기 위한 로직
    void editIncorrectCount(int productCode, int cnt);

    // 노출된 상품들만 조회 (가맹점)
    List<ProductDTO> getAllExposureProduct();

    Boolean postProductWithImage(RequestProduct request, MultipartFile image);
}
