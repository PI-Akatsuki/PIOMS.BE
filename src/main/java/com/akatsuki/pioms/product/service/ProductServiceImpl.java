package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.repository.ProductRepository;
import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.vo.RequestProductPost;
import com.akatsuki.pioms.product.vo.ResponseProductPost;
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
    public ResponseProductPost postProduct(RequestProductPost request) {
        Product product = new Product();

        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setCategory_third_code(request.getCategory_third_code());
        product.setCategoryThirdCode(categoryThird);

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

        ResponseProductPost responseValue =
                new ResponseProductPost(
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
    public ResponseProductPost updateProduct(int productCode, RequestProductPost request) {
        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setCategory_third_code(request.getCategory_third_code());
        product.setCategoryThirdCode(categoryThird);

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

        ResponseProductPost responseValue =
                new ResponseProductPost(
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

//    @Override
//    public RequestProductPost getProductsByCategoryThirdCode(Integer categoryThirdCode) {
//        List<Product> productList = productRepository.findByCategoryThird_CategoryThirdCode(categoryThirdCode);
//        return getAllProduct(productList);
//    }
//
//    private RequestProductPost getAllProduct(List<Product> productList) {
//        List<ResponseProductPost> responseProductList = new ArrayList<>();
//        productList.forEach(product -> responseProductList.add(new ResponseProductPost(product)));
//        return new RequestProductPost(responseProductList);
//    }

//    @Override
//    public List<Product> getCategoryProduct(int categoryThirdCode) {
//        return productRepository.findProductsByCategoryThirdCode(categoryThirdCode);
//    }

//    @Override
//    public List<Product> getAllProductsByCategoryThird(int categoryThirdCode) {
//        return productRepository.findByCategoryThirdCode_CategoryThirdCode(categoryThirdCode);
//    }
}
