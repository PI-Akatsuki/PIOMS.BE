package com.akatsuki.pioms.product.service;


import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.entity.EXCHANGE_PRODUCT_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeProductEntity;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.order.entity.Order;
import com.akatsuki.pioms.product.aggregate.ResponseProducts;

import com.akatsuki.pioms.product.repository.ProductRepository;
import com.akatsuki.pioms.categoryThird.aggregate.CategoryThird;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.RequestProduct;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ExchangeService exchangeService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ExchangeService exchangeService) {
        this.productRepository = productRepository;
        this.exchangeService = exchangeService;
    }
  
    @Override
    public Product getProduct(int productId){
        return productRepository.findById(productId).orElseThrow();
    }
  
    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findProductByCode(int productCode) {
        return productRepository.findById(productCode);
    }

    @Override
    @Transactional
    public ResponseProduct postProduct(RequestProduct request) {
        Product product = new Product();

        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setCategoryThirdCode(request.getCategory_third_code());
        product.setCategoryThird(categoryThird);

        product.setProductName(request.getProduct_name());
        product.setProductPrice(request.getProduct_price());
        product.setProductEnrollDate(request.getProduct_enroll_date());
        product.setProductContent(request.getProduct_content());
        product.setProductColor(request.getProduct_color());
        product.setProductSize(request.getProduct_size());
        product.setProductGender(request.getProduct_gender());
        product.setProductTotalCount(request.getProduct_total_count());
        product.setProductStatus(request.getProduct_status());
        product.setProductExposureStatus(request.isProduct_exposure_status());
        product.setProductNoticeCount(request.getProduct_notice_count());
        product.setProductDiscount(request.getProduct_dis_count());
        product.setProductCount(request.getProduct_count());

        Product updatedProduct = productRepository.save(product);

        ResponseProduct responseValue =
                new ResponseProduct(
                        updatedProduct.getProductCode(),
                        updatedProduct.getProductName(),
                        updatedProduct.getProductPrice(),
                        updatedProduct.getProductEnrollDate(),
                        updatedProduct.getProductContent(),
                        updatedProduct.getProductColor(),
                        updatedProduct.getProductSize(),
                        updatedProduct.getProductGender(),
                        updatedProduct.getProductTotalCount(),
                        updatedProduct.getProductStatus(),
                        updatedProduct.isProductExposureStatus(),
                        updatedProduct.getProductNoticeCount(),
                        updatedProduct.getProductDiscount(),
                        updatedProduct.getProductCount()
                );
        return responseValue;
    }

    @Override
    @Transactional
    public Product deleteProduct(int productCode) {
        productRepository.deleteById(productCode);
        return null;
    }

    @Override
    public ResponseProduct updateProduct(int productCode, RequestProduct request) {
        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setCategoryThirdCode(request.getCategory_third_code());
        product.setCategoryThird(categoryThird);

        product.setProductName(request.getProduct_name());
        product.setProductPrice(request.getProduct_price());
        product.setProductEnrollDate(request.getProduct_enroll_date());
        product.setProductContent(request.getProduct_content());
        product.setProductColor(request.getProduct_color());
        product.setProductSize(request.getProduct_size());
        product.setProductGender(request.getProduct_gender());
        product.setProductTotalCount(request.getProduct_total_count());
        product.setProductStatus(request.getProduct_status());
        product.setProductExposureStatus(request.isProduct_exposure_status());
        product.setProductNoticeCount(request.getProduct_notice_count());
        product.setProductDiscount(request.getProduct_dis_count());
        product.setProductCount(request.getProduct_count());
        Product savedProduct = productRepository.save(product);
        Product updatedProduct = productRepository.save(product);

        ResponseProduct responseValue =
                new ResponseProduct(
                        savedProduct.getProductCode(),
                        savedProduct.getProductName(),
                        updatedProduct.getProductPrice(),
                        updatedProduct.getProductEnrollDate(),
                        updatedProduct.getProductContent(),
                        updatedProduct.getProductColor(),
                        updatedProduct.getProductSize(),
                        updatedProduct.getProductGender(),
                        updatedProduct.getProductTotalCount(),
                        updatedProduct.getProductStatus(),
                        updatedProduct.isProductExposureStatus(),
                        updatedProduct.getProductNoticeCount(),
                        updatedProduct.getProductDiscount(),
                        updatedProduct.getProductCount()
                );
        return responseValue;
    }

    @Override

    public void exportProducts(Order order) {
        // 발주 상품에 대해 재고 수정
        order.getOrderProductList().forEach(requestProduct->{
            productMinusCnt(requestProduct.getRequestProductCount(), requestProduct.getProduct());
        });
    }

    @Override
    public void exportExchangeProducts(int exchangeCode) {
        // 교환 상품에 대해서만 처리해야한다.
        List<ExchangeProductEntity> exchangeProductList = exchangeService.getExchangeProductsWithStatus(exchangeCode, EXCHANGE_PRODUCT_STATUS.교환);
        if (exchangeProductList == null) {
            System.out.println("Exchange Products not found!!");
            return;
        }
        exchangeProductList.forEach(requestProduct->{
            productMinusCnt(requestProduct.getExchangeProductNormalCount(), requestProduct.getProduct());
        });
    }

    @Override
    public boolean checkExchangeProduct(Order order, ExchangeDTO exchange) {

        for (int i = 0; i < exchange.getExchangeProducts().size(); i++) {
            if (exchange.getExchangeProducts().get(i).getExchangeProductStatus() != EXCHANGE_PRODUCT_STATUS.폐기 ){
                Product product = productRepository.findById(exchange.getExchangeProducts().get(i).getProductCode()).orElseThrow();
                if (product.getProductCount()< exchange.getExchangeProducts().get(i).getProductRemainCnt()){
                    return false;
                }
            }
        }
        exportExchangeProducts(exchange.getExchangeCode());
        return true;
    }

    private void productMinusCnt(int requestProduct, Product requestProduct1) {
        Product product = productRepository.findById(requestProduct1.getProductCode()).orElseThrow();
        product.setProductCount(product.getProductCount() - requestProduct);
        productRepository.save(product);
    }


    public List<ResponseProducts> getCategoryProductList(int categoryThirdCode) {
        List<Product> products = productRepository.findAllByCategoryThirdCategoryThirdCode(categoryThirdCode);
        List<ResponseProducts> responseProducts = new ArrayList<>();
        products.forEach(product -> {
            responseProducts.add(new ResponseProducts(product));
        });
        return responseProducts;
    }


}
