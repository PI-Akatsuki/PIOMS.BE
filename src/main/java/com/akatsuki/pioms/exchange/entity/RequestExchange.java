package com.akatsuki.pioms.exchange.entity;


import com.akatsuki.pioms.exchange.vo.ExchangeProductVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestExchange {
    private EXCHANGE_STATUS exchangeStatus;
    List<ExchangeProductVO> products;
}
