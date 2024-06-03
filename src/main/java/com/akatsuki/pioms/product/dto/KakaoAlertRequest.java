package com.akatsuki.pioms.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoAlertRequest {
    private String phoneNumber;
    private String productName;
    private int stockQuantity;
}
