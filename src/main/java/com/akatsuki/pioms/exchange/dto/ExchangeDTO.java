package com.akatsuki.pioms.exchange.dto;

import com.akatsuki.pioms.exchange.entity.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.franchise.entity.FranchiseEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDTO {
    private int exchangeCode;

    private LocalDateTime exchangeDate;

    @Enumerated(EnumType.ORDINAL)
    private EXCHANGE_STATUS exchangeStatus;

    private FranchiseEntity franchise;

    public ExchangeDTO(ExchangeEntity exchange) {
        this.exchangeCode = exchange.getExchangeCode();
        this.exchangeDate = exchange.getExchangeDate();
        this.exchangeStatus = exchange.getExchangeStatus();
        this.franchise = exchange.getFranchise();
    }
}