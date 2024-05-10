package com.akatsuki.pioms.exchange.dto;

import com.akatsuki.pioms.exchange.entity.EXCHANGE_PRODUCT_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExchangeProduct {
    private  int code;
    private int requestCnt;
    private int productRemainCnt;
    public EXCHANGE_PRODUCT_STATUS exchangeProductStatus;

    public ExchangeProduct(ExchangeProductEntity exchangeProductEntity) {
        this.code = exchangeProductEntity.getProduct().getProductCode();
        this.requestCnt = exchangeProductEntity.getExchangeProductCount();
        this.productRemainCnt = exchangeProductEntity.getProduct().getProductCount();
        this.exchangeProductStatus = exchangeProductEntity.getExchangeProductStatus();
    }
}
