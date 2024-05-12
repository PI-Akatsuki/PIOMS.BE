package com.akatsuki.pioms.exchange.aggregate;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseExchange {
    private int exchangeCode;
    private LocalDateTime exchangeDate;
    private EXCHANGE_STATUS exchangeStatus;

    private int franchiseCode;
    private String franchiseName;

    List<ExchangeProductVO> products;

    public ResponseExchange(ExchangeEntity exchangeEntity) {
        this.exchangeCode = exchangeEntity.getExchangeCode();
        this.exchangeDate = exchangeEntity.getExchangeDate();
        this.exchangeStatus = exchangeEntity.getExchangeStatus();

        this.franchiseCode = exchangeEntity.getFranchise().getFranchiseCode();
        this.franchiseName = exchangeEntity.getFranchise().getFranchiseName();
        this.products = new ArrayList<>();
        exchangeEntity.getProducts().forEach(product -> {
            products.add(new ExchangeProductVO(product));
        });

    }
}
