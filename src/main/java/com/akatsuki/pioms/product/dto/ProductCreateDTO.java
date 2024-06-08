package com.akatsuki.pioms.product.dto;

import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import com.akatsuki.pioms.product.etc.PRODUCT_STATUS;
import lombok.*;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@ToString
public class ProductCreateDTO {
    private String productName;
    private int productPrice;
    private String productContent;
    private boolean productExposureStatus;
    private int productDiscount;
    private PRODUCT_COLOR productColor;
    private int productSize;
    private int productTotalCount;
    private PRODUCT_STATUS productStatus;
    private int productNoticeCount;
    private int productCount;
}
