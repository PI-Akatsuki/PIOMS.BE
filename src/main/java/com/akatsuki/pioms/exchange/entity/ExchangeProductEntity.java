package com.akatsuki.pioms.exchange.entity;


import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "exchange_product")
public class ExchangeProductEntity {
    @Id
    @Column(name = "exchange_product_code")
    private int exchangeProductCode;
    @Column(name = "exchange_product_status")
    private EXCHANGE_PRODUCT_STATUS exchangeProductStatus;
    @Column(name = "exchange_product_count")
    private int exchangeProductCount;
    @Column(name = "exchange_product_dis_count")
    private int exchangeProductDiscount;
    @Column(name = "exchange_product_normal_count")
    private int exchangeProductNormalCount;

//    @JoinColumn(name = "product_code")
//    @ManyToOne
    @Column(name = "product_code")
    private int productCode;

    @JoinColumn(name = "exchange_code")
    @ManyToOne
    private ExchangeEntity exchange;
}
