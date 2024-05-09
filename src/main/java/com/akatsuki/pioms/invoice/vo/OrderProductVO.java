package com.akatsuki.pioms.invoice.vo;

import com.akatsuki.pioms.order.entity.OrderProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderProductVO {
    private String productName;
    private int requestProductCount;

    public OrderProductVO(String productName, int requestProductCount) {
        this.productName = productName;
        this.requestProductCount = requestProductCount;
    }
}
