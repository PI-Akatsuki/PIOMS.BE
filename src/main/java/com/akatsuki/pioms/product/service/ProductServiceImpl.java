package com.akatsuki.pioms.product.service;

import com.akatsuki.pioms.product.entity.ProductEntity;
import com.akatsuki.pioms.product.repository.ProductRepository;
import com.akatsuki.pioms.category.entity.CategoryThird;
import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.repository.ProductDAO;
import com.akatsuki.pioms.product.vo.RequestProductPost;
import com.akatsuki.pioms.product.vo.ResponseProductPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductDAO productDAO;
    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductDAO productDAO,ProductRepository productRepository) {
        this.productDAO = productDAO;
        this.productRepository = productRepository;
    }
  
    @Override
    public ProductEntity getProduct(int productId){
        return productRepository.findById(productId).orElseThrow();
    }
    @Override
    public List<Product> getAllProduct() {
        return productDAO.findAll();
    }

    @Override
    public Optional<Product> findProductByCode(int productCode) {
        return productDAO.findById(productCode);
    }

    @Override
    @Transactional
    public ResponseProductPost postProduct(RequestProductPost request) {
        Product product = new Product();
        CategoryThird categoryThird = new CategoryThird();
        categoryThird.setCategory_third_code(request.getCategory_third_code());
        product.setCategory_third_code(categoryThird);

        product.setProductName(request.getProduct_name());
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

        Product savedProduct = productDAO.save(product);

        ResponseProductPost responseValue =
                new ResponseProductPost(
                        savedProduct.getProductCode(),
                        savedProduct.getProductName(),
                        savedProduct.getProduct_price(),
                        savedProduct.getProduct_enroll_date(),
                        savedProduct.getProduct_content(),
                        savedProduct.getProduct_color(),
                        savedProduct.getProduct_size(),
                        savedProduct.getProduct_gender(),
                        savedProduct.getProduct_total_count(),
                        savedProduct.getProduct_status(),
                        savedProduct.isProduct_exposure_status(),
                        savedProduct.getProduct_notice_count(),
                        savedProduct.getProduct_dis_count(),
                        savedProduct.getProduct_count()
                );
        return responseValue;
    }
}
