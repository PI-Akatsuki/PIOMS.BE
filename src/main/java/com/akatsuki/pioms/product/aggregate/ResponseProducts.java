package com.akatsuki.pioms.product.aggregate;

import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProducts {

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
    private int productDiscount;
    private int productCount;
    private int categoryThirdCode;

    public ResponseProducts(Product products) {
        this.productCode = products.getProductCode();
        this.productName = products.getProductName();
        this.productPrice = products.getProductPrice();
        this.productEnrollDate = products.getProductEnrollDate();
        this.productUpdateDate = products.getProductUpdateDate();
        this.productContent = products.getProductContent();
        this.productColor = products.getProductColor();
        this.productSize = products.getProductSize();
        this.productGender = products.getProductGender();
        this.productTotalCount = products.getProductTotalCount();
        this.productStatus = products.getProductStatus();
        this.productExposureStatus = products.isProductExposureStatus();
        this.productNoticeCount = products.getProductNoticeCount();
        this.productDiscount = products.getProductDiscount();
        this.productCount = products.getProductCount();
        this.categoryThirdCode = products.getCategoryThird().getCategoryThirdCode();
    }

}
