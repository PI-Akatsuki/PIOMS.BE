package com.akatsuki.pioms.exchange.dto;

import com.akatsuki.pioms.exchange.aggregate.EXCHANGE_PRODUCT_STATUS;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExchangeProductDTO {
    private int exchangeProductCode;
    private  int productCode;
    private int exchangeProductCount;
    private int productCount;
    private int exchangeProductNormalCount;
    private int exchangeProductDiscount;
    public EXCHANGE_PRODUCT_STATUS exchangeProductStatus;

    public ExchangeProductDTO(ExchangeProduct exchangeProductEntity) {
        this.exchangeProductCode = exchangeProductEntity.getExchangeProductCode();
        this.productCode = exchangeProductEntity.getProduct().getProductCode();
        this.exchangeProductCount = exchangeProductEntity.getExchangeProductCount();
        this.productCount = exchangeProductEntity.getProduct().getProductCount();
        this.exchangeProductStatus = exchangeProductEntity.getExchangeProductStatus();
        this.exchangeProductNormalCount = exchangeProductEntity.getExchangeProductNormalCount();
        this.exchangeProductDiscount = exchangeProductEntity.getExchangeProductDiscount();
    }
}
