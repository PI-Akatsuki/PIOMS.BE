package com.akatsuki.pioms.exchange.dto;

import com.akatsuki.pioms.exchange.aggregate.EXCHANGE_PRODUCT_STATUS;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExchangeProduct {
    private int exchangeProductCode;
    private  int productCode;
    private int requestCnt;
    private int productRemainCnt;
    public EXCHANGE_PRODUCT_STATUS exchangeProductStatus;

    public ExchangeProduct(ExchangeProductEntity exchangeProductEntity) {
        this.exchangeProductCode = exchangeProductEntity.getExchangeProductCode();
        this.productCode = exchangeProductEntity.getProduct().getProductCode();
        this.requestCnt = exchangeProductEntity.getExchangeProductCount();
        this.productRemainCnt = exchangeProductEntity.getProduct().getProductCount();
        this.exchangeProductStatus = exchangeProductEntity.getExchangeProductStatus();
    }
}
