package com.akatsuki.pioms.product.service;

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

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
        categoryThird.setCategory_third_code(request.getCategory_third_code());
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
        categoryThird.setCategory_third_code(request.getCategory_third_code());
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
    public List<ResponseProducts> getCategoryProductList(int categoryThirdCode) {
        List<Product> products = productRepository.findAllByCategoryThirdCategoryThirdCode(categoryThirdCode);
        List<ResponseProducts> responseProducts = new ArrayList<>();
        products.forEach(product -> {
            responseProducts.add(new ResponseProducts(product));
        });
        return responseProducts;
    }

}
