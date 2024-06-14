package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.categoryThird.repository.CategoryThirdRepository;
import com.akatsuki.pioms.config.KakaoProperties;
import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.image.aggregate.Image;
import com.akatsuki.pioms.image.service.ImageService;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.aggregate.EXCHANGE_PRODUCT_STATUS;
import com.akatsuki.pioms.exchange.dto.ExchangeProductDTO;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProductWithImage;
import com.akatsuki.pioms.product.dto.ProductCreateDTO;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.dto.ProductUpdateDTO;
import com.akatsuki.pioms.product.repository.ProductRepository;
import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryThirdRepository categoryThirdRepository;
    private final AdminRepository adminRepository;
    private final LogService logService;
    private final ImageService googleImage;
    private final KakaoProperties kakaoProperties;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryThirdRepository categoryThirdRepository, AdminRepository adminRepository, LogService logService, ImageService googleImage, KakaoProperties kakaoProperties) {
        this.productRepository = productRepository;
        this.categoryThirdRepository = categoryThirdRepository;
        this.adminRepository = adminRepository;
        this.logService = logService;
        this.googleImage = googleImage;
        this.kakaoProperties = kakaoProperties;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProduct() {
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> responseProduct = new ArrayList<>();

        productList.forEach(product -> responseProduct.add(new ProductDTO(product)));
        return responseProduct;
    }

    @Override
    @Transactional
    public List<ProductDTO> findProductByCode(int productCode) {
        List<Product> productList = productRepository.findByProductCode(productCode);
        List<ProductDTO> productDTOS = new ArrayList<>();
        productList.forEach(product -> productDTOS.add(new ProductDTO(product)));
        return productDTOS;
    }

    @Override
    @Transactional
    public ResponseEntity<String> postProduct(RequestProduct request) {
        Product product = new Product();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        List<CategoryThird> categoryThirdList = categoryThirdRepository.findByCategoryThirdCode(request.getCategoryThirdCode());

        if (categoryThirdList == null || categoryThirdList.isEmpty()) {
            return ResponseEntity.badRequest().body("해당 카테고리가 존재하지 않습니다. 다시 확인해주세요.");
        }

        CategoryThird categoryThird = categoryThirdList.get(0);
        product.setCategoryThird(categoryThird);

        product.setProductName(request.getProductName());
        product.setProductPrice(request.getProductPrice());
        product.setProductContent(request.getProductContent());
        product.setProductEnrollDate(formattedDateTime);
        product.setProductUpdateDate(formattedDateTime);
        product.setProductColor(request.getProductColor());
        product.setProductSize(request.getProductSize());
        product.setProductGender(request.getProductGender());
        product.setProductTotalCount(request.getProductTotalCount());
        product.setProductStatus(request.getProductStatus());
        product.setProductExposureStatus(request.isProductExposureStatus());
        product.setProductNoticeCount(request.getProductNoticeCount());
        product.setProductDiscount(request.getProductDisCount());
        product.setProductCount(request.getProductCount());

        Product updatedProduct = productRepository.save(product);

        logService.saveLog("root", LogStatus.등록, updatedProduct.getProductName(), "Product");

        return ResponseEntity.ok("상품 등록 완료!");
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteProduct(int productCode) {

        Product product = productRepository.findById(productCode).orElseThrow(() -> new EntityNotFoundException("해당 상품이 없습니다."));

        // 상품이 존재하지 않으면 에러 메시지 반환
        if (product == null) {
            return ResponseEntity.badRequest().body("해당 상품이 없습니다.");
        }

        // 로그 저장

        String productName = product.getProductName();
        logService.saveLog("root", LogStatus.삭제, productName, "Product");

        // 상품 노출 상태 반전
        boolean currentStatus = product.isProductExposureStatus();
        product.setProductExposureStatus(!currentStatus);
        productRepository.save(product);

        // 변경된 노출 상태에 따라 메시지 설정
        String newStatus = currentStatus ? "미노출" : "노출";
        return ResponseEntity.ok("상품의 노출상태가 " + newStatus + "로 변경되었습니다.");
    }

    @Override
    public List<ProductDTO> getAllExposureProduct() {
        List<Product> productList = productRepository.findAllByProductExposureStatusTrue();
        List<ProductDTO> responseProduct = new ArrayList<>();

        productList.forEach(product -> responseProduct.add(new ProductDTO(product)));
        return responseProduct;
    }

    @Override
    public boolean checkOrderEnable(Map<Integer, Integer> orderProductMap) {
        // 상품 주문 가능 여부 판단하기 위한 로직
        if(orderProductMap==null)
            return false;
        for( int key : orderProductMap.keySet() ){
            Product product = productRepository.findById(key).orElse(null);
            if (product==null || product.getProductCount()<orderProductMap.get(key) || !product.isProductExposureStatus() )
                return false;
        }
        return true;
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateProduct(int productCode, RequestProduct request) {

        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        List<CategoryThird> categoryThirdList = categoryThirdRepository.findByCategoryThirdCode(request.getCategoryThirdCode());

        if (categoryThirdList == null || categoryThirdList.isEmpty()) {
            return ResponseEntity.badRequest().body("해당 카테고리가 존재하지 않습니다. 다시 확인해주세요.");
        }

        CategoryThird categoryThird = categoryThirdList.get(0);
        product.setCategoryThird(categoryThird);

        int oldProductCount = product.getProductCount();

        product.setProductName(request.getProductName());
        product.setProductPrice(request.getProductPrice());
        product.setProductContent(request.getProductContent());
        product.setProductUpdateDate(formattedDateTime);
        product.setProductColor(request.getProductColor());
        product.setProductSize(request.getProductSize());
        product.setProductGender(request.getProductGender());
        product.setProductTotalCount(request.getProductTotalCount());
        product.setProductStatus(request.getProductStatus());
        product.setProductExposureStatus(request.isProductExposureStatus());
        product.setProductNoticeCount(request.getProductNoticeCount());
        product.setProductDiscount(request.getProductDisCount());
        product.setProductCount(request.getProductCount());

        Product updatedProduct = productRepository.save(product);


        // 로그 추가
        System.out.println("Old Product Count: " + oldProductCount);
        System.out.println("Updated Product Count: " + updatedProduct.getProductCount());

        // 재고가 100이하로 떨어지면 알림 전송
        int threshold = 100;
        if (updatedProduct.getProductCount() <= threshold) {
            try {
                sendKakaoAlert(updatedProduct.getProductName(), updatedProduct.getProductCount());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.수정, updatedProduct.getProductName(), "Product");

        return ResponseEntity.ok("상품 수정 완료!");
    }


    @Override
    @Transactional
    public void exportProducts(OrderDTO order) {
        // 발주 상품에 대해 재고 수정
        order.getOrderProductList().forEach(requestProduct -> {
            try {
                productMinusCnt(requestProduct.getRequestProductCount(), requestProduct.getProductCode());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Transactional
    public void importExchangeProducts(RequestExchange requestExchange) {
        if (requestExchange.getProducts() == null || requestExchange.getProducts().isEmpty()) {
            return;
        }
        requestExchange.getProducts().forEach(exchangeProductVO -> {
            int productCode = exchangeProductVO.getProductCode();
            int count = exchangeProductVO.getExchangeProductNormalCount();
            productPlusCnt(productCode, count);
            count = exchangeProductVO.getExchangeProductDiscount();
            productPlusDisCnt(productCode, count);
        });
    }

    private void productPlusDisCnt(int productCode, int count) {
        Product product = productRepository.findById(productCode).orElseThrow();
        product.setProductDiscount(product.getProductDiscount() + count);
        productRepository.save(product);
    }


    public void productPlusCnt(int productCode, int count) {
        Product product = productRepository.findById(productCode).orElseThrow();
        product.setProductCount(product.getProductCount() + count);
        productRepository.save(product);
    }

    @Override
    public boolean checkExchangeProduct(OrderDTO order, ExchangeDTO exchange) {
        if (exchange == null) {
            return false;
        }

        for (ExchangeProductDTO exchangeProduct : exchange.getExchangeProducts()) {
            if (exchangeProduct.getExchangeProductStatus() != EXCHANGE_PRODUCT_STATUS.폐기) {
                Product product = productRepository.findById(exchangeProduct.getProductCode()).orElseThrow();
                if (product.getProductCount() < exchangeProduct.getProductCount()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void productMinusCnt(int requestProduct, int orderProductCode) throws JsonProcessingException {
        Product product = productRepository.findById(orderProductCode).orElseThrow();
        product.setProductCount(product.getProductCount() - requestProduct);
        productRepository.save(product);

        // 재고가 100하로 떨어지면 알림 전송
        int threshold = 100;
        if (product.getProductCount() <= threshold) {
            sendKakaoAlert(product.getProductName(), product.getProductCount());
        }
    }

    @Override
    @Transactional
    public List<ResponseProduct> getCategoryProductList(int categoryThirdCode) {
        List<Product> products = productRepository.findAllByCategoryThirdCategoryThirdCode(categoryThirdCode);
        List<ResponseProduct> responseProducts = new ArrayList<>();
        products.forEach(product -> responseProducts.add(new ResponseProduct(product)));
        return responseProducts;
    }

    @Override
    public String postProductWithImage(RequestProduct request, MultipartFile image) {

        ProductDTO productDTO = postProduct2(request, 1);
        return googleImage.uploadImage(productDTO.getProductCode(), image);

    }

    @Transactional
    public ProductDTO postProduct2(RequestProduct request, int requesterAdminCode) {
        Optional<Admin> requestorAdmin = adminRepository.findById(requesterAdminCode);

        Product product = new Product();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        List<CategoryThird> categoryThirdList = categoryThirdRepository.findByCategoryThirdCode(request.getCategoryThirdCode());

        CategoryThird categoryThird = categoryThirdList.get(0);
        product.setCategoryThird(categoryThird);

        product.setProductName(request.getProductName());
        product.setProductPrice(request.getProductPrice());
        product.setProductContent(request.getProductContent());
        product.setProductEnrollDate(formattedDateTime);
        product.setProductUpdateDate(formattedDateTime);
        product.setProductColor(request.getProductColor());
        product.setProductSize(request.getProductSize());
        product.setProductGender(request.getProductGender());
        product.setProductTotalCount(request.getProductTotalCount());
        product.setProductStatus(request.getProductStatus());
        product.setProductExposureStatus(request.isProductExposureStatus());
        product.setProductNoticeCount(request.getProductNoticeCount());
        product.setProductDiscount(request.getProductDisCount());
        product.setProductCount(request.getProductCount());

        Product updatedProduct = productRepository.save(product);

        logService.saveLog("root", LogStatus.등록, updatedProduct.getProductName(), "Product");

        return new ProductDTO(updatedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseProductWithImage> getAllProductWithImage() {
        List<ProductDTO> productDTOS = getAllProduct();
        List<ResponseProductWithImage> responseProductWithImages = new ArrayList<>();
        for (ProductDTO product : productDTOS) {
            Image image = googleImage.getImageByProductCode(product.getProductCode());
            if (image != null) {
                responseProductWithImages.add(new ResponseProductWithImage(product, image));
                continue;
            }
            responseProductWithImages.add(new ResponseProductWithImage(product));
        }

        return responseProductWithImages;
    }

    @Override
    public void sendKakaoAlert(String productName, int stockQuantity) throws JsonProcessingException {
        String url = kakaoProperties.getUrl() + "/v2/api/talk/memo/default/send";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(kakaoProperties.getToken());

        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", productName + "의 재고가 " + stockQuantity + "개 남았습니다.");
        Map<String, String> linkObject = new HashMap<>();
        linkObject.put("web_url", "http://pioms.shop");
        linkObject.put("mobile_web_url", "http://pioms.shop");
        templateObject.put("link", linkObject);
        templateObject.put("button_title", "바로 확인");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonTemplateObject = objectMapper.writeValueAsString(templateObject);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("template_object", jsonTemplateObject);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            // sout
            System.out.println("Sending Kakao alert with the following details:");
            System.out.println("URL: " + url);
            System.out.println("Headers: " + headers);
            System.out.println("Request Body: " + requestBody);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("알림톡 전송 성공");
            } else {
                System.out.println("알림톡 전송 실패: " + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    @Transactional
    public ResponseEntity<String> updateProductWithImage(int productCode, RequestProduct request) {

        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        List<CategoryThird> categoryThirdList = categoryThirdRepository.findByCategoryThirdCode(request.getCategoryThirdCode());

        if (categoryThirdList == null || categoryThirdList.isEmpty()) {
            return ResponseEntity.badRequest().body("해당 카테고리가 존재하지 않습니다. 다시 확인해주세요.");
        }

        CategoryThird categoryThird = categoryThirdList.get(0);
        product.setCategoryThird(categoryThird);

        int oldProductCount = product.getProductCount();

        product.setProductName(request.getProductName());
        product.setProductPrice(request.getProductPrice());
        product.setProductContent(request.getProductContent());
        product.setProductUpdateDate(formattedDateTime);
        product.setProductColor(request.getProductColor());
        product.setProductSize(request.getProductSize());
        product.setProductGender(request.getProductGender());
        product.setProductTotalCount(request.getProductTotalCount());
        product.setProductStatus(request.getProductStatus());
        product.setProductExposureStatus(request.isProductExposureStatus());
        product.setProductNoticeCount(request.getProductNoticeCount());
        product.setProductDiscount(request.getProductDisCount());
        product.setProductCount(request.getProductCount());

        Product updatedProduct = productRepository.save(product);
        if(!request.getFile().isEmpty()){
            googleImage.changeImage(productCode,request.getFile());
        }

        // 로그 추가
        System.out.println("Old Product Count: " + oldProductCount);
        System.out.println("Updated Product Count: " + updatedProduct.getProductCount());

        // 재고가 100이하로 떨어지면 알림 전송
        int threshold = 100;
        if (updatedProduct.getProductCount() <= threshold) {
            try {
                sendKakaoAlert(updatedProduct.getProductName(), updatedProduct.getProductCount());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.수정, updatedProduct.getProductName(), "Product");

        return ResponseEntity.ok("상품 수정 완료!");
    }

    @Override
    public List<ProductDTO> findNotEnoughProducts() {
        List<Product> products = productRepository.findNotEnoughProducts();
        List<ProductDTO> productDTOS = new ArrayList<>();
        products.forEach(product -> productDTOS.add(new ProductDTO(product)));
        return productDTOS;
    }

    @Override
    public int getOrderTotalPrice(Map<Integer,Integer> requestOrderVO) {

        var ref = new Object() {
            int totalPrice = 0;
        };
        requestOrderVO.forEach((code,cnt)->{
            ref.totalPrice += productRepository.findById(code).get().getProductPrice()*cnt;
            System.out.println("ref = " + ref.totalPrice);
        });
        System.out.println("ref = " + ref.totalPrice);
        return ref.totalPrice;
    }

    @Override
    public ProductDTO createProduct(ProductCreateDTO productCreateDTO) {
        Product product = new Product();
        product.setProductName(productCreateDTO.getProductName());
        product.setProductPrice(productCreateDTO.getProductPrice());
        product.setProductContent(productCreateDTO.getProductContent());
        product.setProductExposureStatus(productCreateDTO.isProductExposureStatus());
        product.setProductDiscount(productCreateDTO.getProductDiscount());
        product.setProductColor(productCreateDTO.getProductColor());
        product.setProductSize(productCreateDTO.getProductSize());
        product.setProductTotalCount(productCreateDTO.getProductTotalCount());
        product.setProductStatus(productCreateDTO.getProductStatus());
        product.setProductNoticeCount(productCreateDTO.getProductNoticeCount());
        product.setProductCount(productCreateDTO.getProductCount());

        productRepository.save(product);
        logService.saveLog("root", LogStatus.등록, product.getProductName(), "Product");
        return new ProductDTO(product);
    }

    @Override
    public Product modifyProduct(int productCode, ProductUpdateDTO productUpdateDTO) {
        Product product = productRepository.findById(productCode)
                .orElseThrow(()-> new EntityNotFoundException("Product not found with id:" + productCode));
        product.setProductName(productUpdateDTO.getProductName());
        product.setProductPrice(productUpdateDTO.getProductPrice());
        product.setProductContent(productUpdateDTO.getProductContent());
        product.setProductExposureStatus(productUpdateDTO.isProductExposureStatus());
        product.setProductDiscount(productUpdateDTO.getProductDiscount());
        product.setProductColor(productUpdateDTO.getProductColor());
        product.setProductSize(productUpdateDTO.getProductSize());
        product.setProductTotalCount(productUpdateDTO.getProductTotalCount());
        product.setProductStatus(productUpdateDTO.getProductStatus());
        product.setProductNoticeCount(productUpdateDTO.getProductNoticeCount());
        product.setProductCount(productUpdateDTO.getProductCount());

        productRepository.save(product);
        logService.saveLog("root", LogStatus.수정, product.getProductName(), "Product");
        return productRepository.save(product);
    }
}
