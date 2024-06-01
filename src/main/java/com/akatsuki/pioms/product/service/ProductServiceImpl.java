package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.categoryThird.repository.CategoryThirdRepository;
import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.image.aggregate.Image;
import com.akatsuki.pioms.image.service.ImageService;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.aggregate.EXCHANGE_PRODUCT_STATUS;
import com.akatsuki.pioms.exchange.dto.ExchangeProductDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;

import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProductWithImage;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.repository.ProductRepository;
import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryThirdRepository categoryThirdRepository;
    private final AdminRepository adminRepository;
    private final LogService logService;
    private final ImageService googleImage;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryThirdRepository categoryThirdRepository, AdminRepository adminRepository, LogService logService, ImageService googleImage) {
        this.productRepository = productRepository;
        this.categoryThirdRepository = categoryThirdRepository;
        this.adminRepository = adminRepository;
        this.logService = logService;
        this.googleImage = googleImage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProduct() {
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> responseProduct = new ArrayList<>();

        productList.forEach(product -> {
            responseProduct.add(new ProductDTO(product));
        });
        return responseProduct;
    }

    @Override
    @Transactional
    public List<ProductDTO> findProductByCode(int productCode) {
        List<Product> productList = productRepository.findByProductCode(productCode);
        List<ProductDTO> productDTOS = new ArrayList<>();
        productList.forEach(product -> {
            productDTOS.add(new ProductDTO(product));
        });
        return productDTOS;
    }

    @Override
    @Transactional
    public ResponseEntity<String> postProduct(RequestProduct request) {

        Product product = new Product();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        List<CategoryThird> categoryThirdList = categoryThirdRepository.findByCategoryThirdCode(request.getCategoryThirdCode());

        if(categoryThirdList == null) {
            return ResponseEntity.badRequest().body("해당 카테고리가 존재하지 않습니다. 다시 확인해주세요.");
        }

        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setCategoryThirdCode(request.getCategoryThirdCode());
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        logService.saveLog(username, LogStatus.등록, updatedProduct.getProductName(), "Product");

        return ResponseEntity.ok("상품 등록 완료!");
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteProduct(int productCode) {
        Product product = productRepository.findById(productCode).orElseThrow(()-> new EntityNotFoundException("그런거 없다."));
        if(product == null) {
            return ResponseEntity.badRequest().body("해당 상품이 없습니다.");
        }
        String productName = product.getProductName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.삭제, productName, "Product");

        if (!product.isProductExposureStatus()) {
            product.setProductExposureStatus(true);
            productRepository.save(product);
            return ResponseEntity.ok("상품의 노출상태가 변경되었습니다.");
        } else {
            return ResponseEntity.ok("해당 상품은 이미 비노출상태의 상품입니다.");
        }
    }

    @Override
    public List<ProductDTO> getAllExposureProduct() {
        List<Product> productList = productRepository.findAllByProductExposureStatusTrue();
        List<ProductDTO> responseProduct = new ArrayList<>();

        productList.forEach(product -> {
            responseProduct.add(new ProductDTO(product));
        });
        return responseProduct;
    }

    @Override
    public boolean checkOrderEnable(Map<Integer, Integer> orderProductMap) {
        return false;
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateProduct(int productCode, RequestProduct request) {
        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품이 존재하지 않습니다."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setCategoryThirdCode(request.getCategoryThirdCode());
        product.setCategoryThird(categoryThird);

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        logService.saveLog(username, LogStatus.수정, updatedProduct.getProductName(), "Product");

        return ResponseEntity.ok("상품 수정 완료!");
    }

    @Override
    @Transactional
    public void exportProducts(OrderDTO order) {
        // 발주 상품에 대해 재고 수정
        order.getOrderProductList().forEach(requestProduct->{
            productMinusCnt(requestProduct.getRequestProductCount(), requestProduct.getProductCode());
        });
    }
    @Override
    @Transactional
    public void importExchangeProducts(RequestExchange requestExchange) {
        if(requestExchange.getProducts() == null || requestExchange.getProducts().isEmpty()){
            return;
        }
        requestExchange.getProducts().forEach(exchangeProductVO -> {
            int productCode = exchangeProductVO.getProductCode();
            int count = exchangeProductVO.getExchangeProductNormalCount();
            productPlusCnt(productCode,count);
            count = exchangeProductVO.getExchangeProductDiscount();
            productPlusDisCnt(productCode,count);
        });
    }

    private void productPlusDisCnt(int productCode,int count) {
        Product product = productRepository.findById(productCode).orElseThrow();
        product.setProductDiscount(product.getProductDiscount()+count);
        productRepository.save(product);
    }

    public void productPlusCnt(int productCode, int count) {
        Product product = productRepository.findById(productCode).orElseThrow();
        product.setProductCount(product.getProductCount()+count);
        productRepository.save(product);
    }


    @Override
    public boolean checkExchangeProduct(OrderDTO order, ExchangeDTO exchange) {
        if (exchange== null){
            return false;
        }

        for (int i = 0; i < exchange.getExchangeProducts().size(); i++) {
            if (exchange.getExchangeProducts().get(i).getExchangeProductStatus() != EXCHANGE_PRODUCT_STATUS.폐기 ){
                Product product = productRepository.findById(exchange.getExchangeProducts().get(i).getProductCode()).orElseThrow();
                if (product.getProductCount()< exchange.getExchangeProducts().get(i).getProductCount()){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void productMinusCnt(int requestProduct, int orderProductCode) {
        Product product = productRepository.findById(orderProductCode).orElseThrow();
        product.setProductCount(product.getProductCount() - requestProduct);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public List<ResponseProduct> getCategoryProductList(int categoryThirdCode) {
        List<Product> products = productRepository.findAllByCategoryThirdCategoryThirdCode(categoryThirdCode);
        List<ResponseProduct> responseProducts = new ArrayList<>();
        products.forEach(product -> {
            responseProducts.add(new ResponseProduct(product));
        });
        return responseProducts;
    }


    @Override
    @Transactional
    public Boolean postProductWithImage(RequestProduct request, MultipartFile image) {
        ProductDTO productDTO = postProduct2(request,1);
        return googleImage.uploadImage(productDTO.getProductCode(), image);
    }

    @Transactional
    public ProductDTO postProduct2(RequestProduct request, int requesterAdminCode) {
        Optional<Admin> requestorAdmin = adminRepository.findById(requesterAdminCode);

        Product product = new Product();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);

        List<CategoryThird> categoryThirdList = categoryThirdRepository.findByCategoryThirdCode(request.getCategoryThirdCode());

        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setCategoryThirdCode(request.getCategoryThirdCode());
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
        for (int i = 0; i < productDTOS.size(); i++) {
            ProductDTO product = productDTOS.get(i);
            Image image = googleImage.getImageByProductCode(product.getProductCode());
            if (image!=null){
                responseProductWithImages.add(new ResponseProductWithImage(product,image));
                continue;
            }
            responseProductWithImages.add(new ResponseProductWithImage(product));
        }

        return responseProductWithImages;
    }

}
