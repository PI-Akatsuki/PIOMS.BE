package com.akatsuki.pioms.exchange.aggregate;


import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
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

    public ResponseExchange(Exchange exchangeEntity) {
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

    public ResponseExchange(ExchangeDTO exchangeDTO) {
        this.exchangeCode = exchangeDTO.getExchangeCode();
        this.exchangeDate = exchangeDTO.getExchangeDate();
        this.exchangeStatus = exchangeDTO.getExchangeStatus();

        this.franchiseCode = exchangeDTO.getFranchiseCode();
//        this.franchiseName = exchangeDTO.getFranchiseName();
        this.products = new ArrayList<>();
        exchangeDTO.getExchangeProducts().forEach(product -> {
            products.add(new ExchangeProductVO(product));
        });
    }
}
