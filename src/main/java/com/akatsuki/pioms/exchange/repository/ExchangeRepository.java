package com.akatsuki.pioms.exchange.repository;

import com.akatsuki.pioms.exchange.aggregate.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.aggregate.ExchangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<ExchangeEntity, Integer> {
    ExchangeEntity findByFranchiseFranchiseCodeAndExchangeStatus(int franchiseCode,EXCHANGE_STATUS exchangeStatus);

    ExchangeEntity findByExchangeStatus(EXCHANGE_STATUS exchangeStatus);

    List<ExchangeEntity> findAllByFranchiseFranchiseCodeAndExchangeStatus(int franchiseCode, EXCHANGE_STATUS exchangeStatus);

    void deleteAllByFranchiseFranchiseCodeAndExchangeStatus(int franchiseCode, EXCHANGE_STATUS exchangeStatus);

    List<ExchangeEntity> findAllByFranchiseFranchiseCodeAndExchangeStatusOrderByExchangeDateAsc(int franchiseCode, EXCHANGE_STATUS exchangeStatus);
}
