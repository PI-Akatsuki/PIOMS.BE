package com.akatsuki.pioms.product.dto;

import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@ToString
public class ProductUpdateDTO {
    private String productName;
    private int productPrice;
    private String productContent;
    private PRODUCT_COLOR productColor;
    private int productSize;
    private int productTotalCount;
    private PRODUCT_STATUS productStatus;
    private boolean productExposureStatus;
    private int productNoticeCount;
    private int productDiscount;
    private int productCount;
}
