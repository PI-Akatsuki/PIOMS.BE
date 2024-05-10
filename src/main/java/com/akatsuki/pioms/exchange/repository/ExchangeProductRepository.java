package com.akatsuki.pioms.exchange.repository;

import com.akatsuki.pioms.exchange.entity.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.exchange.entity.ExchangeProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeProductRepository extends JpaRepository<ExchangeProductEntity, Integer> {


//    void deleteAllByExchangeFranchiseFranchiseCodeAndExchangeExchangeStatus(int franchiseCode, EXCHANGE_STATUS exchangeStatus);
    void deleteAllByExchangeFranchiseFranchiseCodeAndExchangeExchangeStatus(int franchiseCode, EXCHANGE_STATUS exchangeStatus);
}