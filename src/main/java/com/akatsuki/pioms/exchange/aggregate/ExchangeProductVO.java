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
    private String productName;

    private int exchangeProductCount;
    private int exchangeProductNormalCount;
    private int exchangeProductDiscount;
    private EXCHANGE_PRODUCT_STATUS exchangeProductStatus;




    public ExchangeProductVO(int exchangeProductCode, int exchangeProductCount, int exchangeProductNormalCount, int exchangeProductDiscount) {
        this.exchangeProductCode = exchangeProductCode;
        this.exchangeProductCount = exchangeProductCount;
        this.exchangeProductNormalCount = exchangeProductNormalCount;
        this.exchangeProductDiscount = exchangeProductDiscount;
    }

    public ExchangeProductVO(int productCode, int exchangeProductCount, EXCHANGE_PRODUCT_STATUS exchangeProductStatus){
        this.productCode = productCode;
        this.exchangeProductCount = exchangeProductCount;
        this.exchangeProductStatus = exchangeProductStatus;
    }

    public ExchangeProductVO(ExchangeProduct product) {
        this.exchangeProductCode = product.getExchangeProductCode();
        this.productCode = product.getProduct().getProductCode();
        this.exchangeProductCount = product.getExchangeProductCount();
        this.exchangeProductNormalCount = product.getExchangeProductNormalCount();
        this.exchangeProductDiscount = product.getExchangeProductDiscount();
        this.exchangeProductStatus = product.getExchangeProductStatus();
        this.productName = product.getProduct().getProductName();
    }


    public ExchangeProductVO(ExchangeProductDTO product) {
        this.exchangeProductCode = product.getExchangeProductCode();
        this.productCode = product.getProductCode();
        this.productName = product.getProductName();
        this.exchangeProductCount = product.getExchangeProductCount();
        this.exchangeProductNormalCount = product.getExchangeProductNormalCount();
        this.exchangeProductDiscount = product.getExchangeProductDiscount();
        this.exchangeProductStatus = product.getExchangeProductStatus();
    }
}
