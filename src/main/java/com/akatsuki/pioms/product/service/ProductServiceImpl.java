package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.repository.ProductRepository;
import com.akatsuki.pioms.product.vo.RequestProductPost;
import com.akatsuki.pioms.product.vo.ResponseProductPost;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        product.setCategory_third_code(categoryThird);

        product.setProduct_name(request.getProduct_name());
        product.setProduct_price(request.getProduct_price());
        product.setProduct_enroll_date(request.getProduct_enroll_date());
        product.setProduct_content(request.getProduct_content());
        product.setProduct_color(request.getProduct_color());
        product.setProduct_size(request.getProduct_size());
        product.setProduct_gender(request.getProduct_gender());
        product.setProduct_total_count(request.getProduct_total_count());
        product.setProduct_status(request.getProduct_status());
        product.setProduct_exposure_status(request.isProduct_exposure_status());
        product.setProduct_notice_count(request.getProduct_notice_count());
        product.setProduct_dis_count(request.getProduct_dis_count());
        product.setProduct_count(request.getProduct_count());

        Product updatedProduct = productRepository.save(product);

        ResponseProductPost responseValue =
                new ResponseProductPost(
                        updatedProduct.getProduct_code(),
                        updatedProduct.getProduct_name(),
                        updatedProduct.getProduct_price(),
                        updatedProduct.getProduct_enroll_date(),
                        updatedProduct.getProduct_content(),
                        updatedProduct.getProduct_color(),
                        updatedProduct.getProduct_size(),
                        updatedProduct.getProduct_gender(),
                        updatedProduct.getProduct_total_count(),
                        updatedProduct.getProduct_status(),
                        updatedProduct.isProduct_exposure_status(),
                        updatedProduct.getProduct_notice_count(),
                        updatedProduct.getProduct_dis_count(),
                        updatedProduct.getProduct_count()
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
        product.setCategory_third_code(categoryThird);

        product.setProduct_name(request.getProduct_name());
        product.setProduct_price(request.getProduct_price());
        product.setProduct_enroll_date(request.getProduct_enroll_date());
        product.setProduct_content(request.getProduct_content());
        product.setProduct_color(request.getProduct_color());
        product.setProduct_size(request.getProduct_size());
        product.setProduct_gender(request.getProduct_gender());
        product.setProduct_total_count(request.getProduct_total_count());
        product.setProduct_status(request.getProduct_status());
        product.setProduct_exposure_status(request.isProduct_exposure_status());
        product.setProduct_notice_count(request.getProduct_notice_count());
        product.setProduct_dis_count(request.getProduct_dis_count());
        product.setProduct_count(request.getProduct_count());

        Product updatedProduct = productRepository.save(product);

        ResponseProductPost responseValue =
                new ResponseProductPost(
                        updatedProduct.getProduct_code(),
                        updatedProduct.getProduct_name(),
                        updatedProduct.getProduct_price(),
                        updatedProduct.getProduct_enroll_date(),
                        updatedProduct.getProduct_content(),
                        updatedProduct.getProduct_color(),
                        updatedProduct.getProduct_size(),
                        updatedProduct.getProduct_gender(),
                        updatedProduct.getProduct_total_count(),
                        updatedProduct.getProduct_status(),
                        updatedProduct.isProduct_exposure_status(),
                        updatedProduct.getProduct_notice_count(),
                        updatedProduct.getProduct_dis_count(),
                        updatedProduct.getProduct_count()
                );
        return responseValue;
    }
}
