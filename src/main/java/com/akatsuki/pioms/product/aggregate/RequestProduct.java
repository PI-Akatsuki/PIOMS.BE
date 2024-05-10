package com.akatsuki.pioms.product.aggregate;

import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
public class RequestProduct {

    private int productCode;
    private String productName;
    private int productPrice;
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

    public RequestProduct(int productCode, String productName, int productPrice, String productContent, PRODUCT_COLOR productColor, int productSize, PRODUCT_GENDER productGender, int productTotalCount, PRODUCT_STATUS productStatus, boolean productExposureStatus, int productNoticeCount, int productDisCount, int productCount, int categoryThirdCode) {
        this.productCode = productCode;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productContent = productContent;
        this.productColor = productColor;
        this.productSize = productSize;
        this.productGender = productGender;
        this.productTotalCount = productTotalCount;
        this.productStatus = productStatus;
        this.productExposureStatus = productExposureStatus;
        this.productNoticeCount = productNoticeCount;
        this.productDisCount = productDisCount;
        this.productCount = productCount;
        this.categoryThirdCode = categoryThirdCode;
    }

    public RequestProduct(List<ResponseProduct> responseProductList) {

    }
}
