package com.akatsuki.pioms.exchange.aggregate;


import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestExchange {

    private EXCHANGE_STATUS exchangeStatus;
    List<ExchangeProductVO> products= new ArrayList<>();
    public RequestExchange(EXCHANGE_STATUS exchangeStatus, ExchangeDTO exchangeDTO) {
        this.exchangeStatus = exchangeStatus;
        exchangeDTO.getExchangeProducts().forEach(
                exchangeProductDTO ->
                    products.add(new ExchangeProductVO(exchangeProductDTO))
        );
    }
}
