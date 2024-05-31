package com.akatsuki.pioms.product.aggregate;

import com.akatsuki.pioms.image.aggregate.Image;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_GENDER;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseProductWithImage {

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
    private String getCategoryThirdName;
    private int categorySecondCode;
    private String getCategorySecondName;
    private int categoryFirstCode;
    private String getCategoryFirstName;

    private String imgUrl;

    public ResponseProductWithImage(ProductDTO productDTO, Image image) {
        this.productCode = productDTO.getProductCode();
        this.productName = productDTO.getProductName();
        this.productPrice = productDTO.getProductPrice();
        this.productEnrollDate = productDTO.getProductEnrollDate();
        this.productUpdateDate = productDTO.getProductUpdateDate();
        this.productContent = productDTO.getProductContent();
        this.productColor = productDTO.getProductColor();
        this.productSize = productDTO.getProductSize();
        this.productGender = PRODUCT_GENDER.valueOf(productDTO.getProductGender());
        this.productTotalCount = productDTO.getProductTotalCount();
        this.productStatus = productDTO.getProductStatus();
        this.productExposureStatus = productDTO.isProductExposureStatus();
        this.productNoticeCount = productDTO.getProductNoticeCount();
        this.productDisCount = productDTO.getProductDiscount();
        this.productCount = productDTO.getProductCount();
        this.categoryThirdCode = productDTO.getCategoryThirdCode();
        this.getCategoryThirdName = productDTO.getCategoryThirdName();
        this.categorySecondCode = productDTO.getCategorySecondCode();
        this.getCategorySecondName = productDTO.getCategorySecondName();
        this.categoryFirstCode = productDTO.getCategoryFirstCode();
        this.getCategoryFirstName = productDTO.getCategoryFirstName();
        this.imgUrl = image.getUrl();
    }
    public ResponseProductWithImage(ProductDTO productDTO) {
        this.productCode = productDTO.getProductCode();
        this.productName = productDTO.getProductName();
        this.productPrice = productDTO.getProductPrice();
        this.productEnrollDate = productDTO.getProductEnrollDate();
        this.productUpdateDate = productDTO.getProductUpdateDate();
        this.productContent = productDTO.getProductContent();
        this.productColor = productDTO.getProductColor();
        this.productSize = productDTO.getProductSize();
        this.productGender = PRODUCT_GENDER.valueOf(productDTO.getProductGender());
        this.productTotalCount = productDTO.getProductTotalCount();
        this.productStatus = productDTO.getProductStatus();
        this.productExposureStatus = productDTO.isProductExposureStatus();
        this.productNoticeCount = productDTO.getProductNoticeCount();
        this.productDisCount = productDTO.getProductDiscount();
        this.productCount = productDTO.getProductCount();
        this.categoryThirdCode = productDTO.getCategoryThirdCode();
        this.getCategoryThirdName = productDTO.getCategoryThirdName();
        this.categorySecondCode = productDTO.getCategorySecondCode();
        this.getCategorySecondName = productDTO.getCategorySecondName();
        this.categoryFirstCode = productDTO.getCategoryFirstCode();
        this.getCategoryFirstName = productDTO.getCategoryFirstName();
    }
}
