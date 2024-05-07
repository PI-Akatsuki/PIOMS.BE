package com.akatsuki.pioms.exchange.repository;

import com.akatsuki.pioms.exchange.entity.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<ExchangeEntity, Integer> {
    ExchangeEntity findByFranchiseFranchiseCodeAndExchangeStatus(int franchiseCode,EXCHANGE_STATUS exchangeStatus);

    ExchangeEntity findByExchangeStatus(EXCHANGE_STATUS exchangeStatus);
}
