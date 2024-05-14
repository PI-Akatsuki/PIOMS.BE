package com.akatsuki.pioms.exchange.aggregate;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestExchange {
    private int franchiseCode;
    private EXCHANGE_STATUS exchangeStatus;
    List<ExchangeProductVO> products;
}
