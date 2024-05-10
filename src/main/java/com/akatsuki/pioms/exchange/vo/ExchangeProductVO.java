package com.akatsuki.pioms.exchange.vo;

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
public class ExchangeProductVO {
    private int exchangeProductCode;
    private int exchangeProductCount;
    private int exchangeProductNormalCount;
    private int exchangeProductDiscount;
    private EXCHANGE_PRODUCT_STATUS exchangeProductStatus;

    public ExchangeProductVO(ExchangeProductEntity product) {
        this.exchangeProductCode = product.getExchangeProductCode();
        this.exchangeProductCount = product.getExchangeProductCount();
        this.exchangeProductNormalCount = product.getExchangeProductNormalCount();
        this.exchangeProductDiscount = product.getExchangeProductDiscount();
        this.exchangeProductStatus = product.getExchangeProductStatus();
    }
}
