package com.akatsuki.pioms.product.aggregate;

import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResponseProduct {

    private int productCode;
    private String productName;
    private int productPrice;
    private String productEnrollDate;
    private String productUpdateDate;
    private String productContent;
    private PRODUCT_COLOR productColor;
    private int productSize;
    private PRODUCT_GENDER productGender;
    private int productTotalCount;
    private PRODUCT_STATUS productStatus;
    private boolean productExposureStatus;
    private int productNoticeCount;
    private int productDisCount;
    private int productCount;
    private int categoryThirdCode;

    public ResponseProduct(Product product) {
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
        this.productEnrollDate = product.getProductEnrollDate();
        this.productUpdateDate = product.getProductUpdateDate();
        this.productContent = product.getProductContent();
        this.productColor = product.getProductColor();
        this.productSize = product.getProductSize();
        this.productGender = product.getProductGender();
        this.productTotalCount = product.getProductTotalCount();
        this.productStatus = product.getProductStatus();
        this.productExposureStatus = product.isProductExposureStatus();
        this.productNoticeCount = product.getProductNoticeCount();
        this.productDisCount = product.getProductDiscount();
        this.productCount = product.getProductCount();
        this.categoryThirdCode = product.getCategoryThird().getCategoryThirdCode();
    }

    public ResponseProduct(ProductDTO productDTO) {
        this.productCode = productDTO.getProductCode();
        this.productName = productDTO.getProductName();
        this.productPrice = productDTO.getProductPrice();
        this.productEnrollDate = productDTO.getProductEnrollDate();
        this.productUpdateDate = productDTO.getProductUpdateDate();
        this.productContent = productDTO.getProductContent();
        this.productColor = productDTO.getProductColor();
        this.productSize = productDTO.getProductSize();
        this.productGender = productDTO.getProductGender();
        this.productTotalCount = productDTO.getProductTotalCount();
        this.productStatus = productDTO.getProductStatus();
        this.productExposureStatus = productDTO.isProductExposureStatus();
        this.productNoticeCount = productDTO.getProductNoticeCount();
        this.productDisCount = productDTO.getProductDiscount();
        this.productCount = productDTO.getProductCount();
        this.categoryThirdCode = productDTO.getCategoryThirdCode();
    }

    public ResponseProduct(int productCode, String productName, int productPrice, String productEnrollDate, String productContent, PRODUCT_COLOR productColor, int productSize, PRODUCT_GENDER productGender, int productTotalCount, PRODUCT_STATUS productStatus, boolean productExposureStatus, int productNoticeCount, int productDiscount, int productCount) {
    }
}
