package com.akatsuki.pioms.exchange.dto;

import com.akatsuki.pioms.exchange.aggregate.EXCHANGE_PRODUCT_STATUS;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProduct;
import com.akatsuki.pioms.product.dto.ProductDTO;
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
//    public ProductDTO product;
    private int productCode;
    private String productName;
    private int productCount;
    private int exchangeProductCount;
    private int exchangeProductNormalCount;
    private int exchangeProductDiscount;
    public EXCHANGE_PRODUCT_STATUS exchangeProductStatus;


    public ExchangeProductDTO(ExchangeProduct exchangeProductEntity) {
        this.exchangeProductCode = exchangeProductEntity.getExchangeProductCode();
        this.productCode = new ProductDTO(exchangeProductEntity.getProduct()).getProductCode();
        this.productName = new ProductDTO(exchangeProductEntity.getProduct()).getProductName();
        this.productCount = new ProductDTO(exchangeProductEntity.getProduct()).getProductCount();
        this.exchangeProductCount = exchangeProductEntity.getExchangeProductCount();
        this.exchangeProductStatus = exchangeProductEntity.getExchangeProductStatus();
        this.exchangeProductNormalCount = exchangeProductEntity.getExchangeProductNormalCount();
        this.exchangeProductDiscount = exchangeProductEntity.getExchangeProductDiscount();
    }
}
