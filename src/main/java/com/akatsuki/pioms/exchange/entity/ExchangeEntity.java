package com.akatsuki.pioms.exchange.entity;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.entity.FranchiseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "exchange")
public class ExchangeEntity {
    @Id
    @Column(name = "exchange_code")
    private int exchangeCode;

    @Column(name = "exchange_date")
    private LocalDateTime exchangeDate;

    @Column(name = "exchange_status")
    @Enumerated(EnumType.STRING)
    private EXCHANGE_STATUS exchangeStatus;

    @JoinColumn(name = "franchise_code")
    @ManyToOne
    private FranchiseEntity franchise;


    public ExchangeEntity(ExchangeDTO exchange) {
        this.exchangeCode = exchange.getExchangeCode();
        this.exchangeDate = exchange.getExchangeDate();
        this.exchangeStatus = exchange.getExchangeStatus();
        this.franchise = exchange.getFranchise();
    }
}
