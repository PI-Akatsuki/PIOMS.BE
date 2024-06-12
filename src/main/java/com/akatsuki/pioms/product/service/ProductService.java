package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProductWithImage;
import com.akatsuki.pioms.product.dto.ProductCreateDTO;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.dto.ProductUpdateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface ProductService {
    List<ProductDTO> getAllProduct();
    List<ProductDTO> findProductByCode(int productCode);
    ResponseEntity<String> postProduct(RequestProduct request);
    ResponseEntity<String> deleteProduct(int productCode);
    ResponseEntity<String> updateProduct(int productCode, RequestProduct request);
    void exportProducts(OrderDTO order);
    boolean checkExchangeProduct(OrderDTO order, ExchangeDTO exchange);

    void productMinusCnt(int requestProduct, int orderProductCode) throws JsonProcessingException;

    List<ResponseProduct> getCategoryProductList(int categoryThirdCode);

    // 노출된 상품들만 조회 (가맹점)
    List<ProductDTO> getAllExposureProduct();

    // 발주 신청 가능 여부 확인
    boolean checkOrderEnable(Map<Integer,Integer> orderProductMap);

    String postProductWithImage(RequestProduct request, MultipartFile image);

    void importExchangeProducts(RequestExchange requestExchange);

    List<ResponseProductWithImage> getAllProductWithImage();

    void productPlusCnt(int productCode, int i);

    // 카카오 알림
    void sendKakaoAlert(String productName, int stockQuantity) throws JsonProcessingException;

    ResponseEntity<String> updateProductWithImage(int productCode, RequestProduct request);

    List<ProductDTO> findNotEnoughProducts();

    int getOrderTotalPrice(Map<Integer,Integer> requestOrderVO);

    ProductDTO createProduct(ProductCreateDTO productCreateDTO);
    Product modifyProduct(int productCode, ProductUpdateDTO productUpdateDTO);
}
