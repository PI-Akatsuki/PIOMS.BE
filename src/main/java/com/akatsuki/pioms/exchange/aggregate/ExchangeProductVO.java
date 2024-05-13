package com.akatsuki.pioms.exchange.aggregate;

import com.akatsuki.pioms.exchange.dto.ExchangeProductDTO;
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
    private int productCode;
    private int exchangeProductCount;
    private int exchangeProductNormalCount;
    private int exchangeProductDiscount;
    private EXCHANGE_PRODUCT_STATUS exchangeProductStatus;

    public ExchangeProductVO(ExchangeProduct product) {
        this.exchangeProductCode = product.getExchangeProductCode();
        this.productCode = product.getProduct().getProductCode();
        this.exchangeProductCount = product.getExchangeProductCount();
        this.exchangeProductNormalCount = product.getExchangeProductNormalCount();
        this.exchangeProductDiscount = product.getExchangeProductDiscount();
        this.exchangeProductStatus = product.getExchangeProductStatus();
    }


    public ExchangeProductVO(ExchangeProductDTO product) {
        this.exchangeProductCode = product.getExchangeProductCode();
        this.productCode = product.getProductCode();
        this.exchangeProductCount = product.getRequestCnt();
        this.exchangeProductNormalCount = product.getExchangeProductNormalCount();
        this.exchangeProductDiscount = product.getExchangeProductDiscount();
        this.exchangeProductStatus = product.getExchangeProductStatus();
    }
}
