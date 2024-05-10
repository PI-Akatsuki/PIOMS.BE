package com.akatsuki.pioms.exchange.entity;


import com.akatsuki.pioms.exchange.vo.ExchangeProductVO;
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
    private EXCHANGE_STATUS exchangeStatus;
    List<ExchangeProductVO> products;
}
