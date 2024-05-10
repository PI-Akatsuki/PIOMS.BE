package com.akatsuki.pioms.exchange.repository;

import com.akatsuki.pioms.exchange.entity.EXCHANGE_PRODUCT_STATUS;
import com.akatsuki.pioms.exchange.entity.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.exchange.entity.ExchangeProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExchangeProductRepository extends JpaRepository<ExchangeProductEntity, Integer> {

    void deleteAllByExchangeFranchiseFranchiseCodeAndExchangeExchangeStatus(int franchiseCode, EXCHANGE_STATUS exchangeStatus);

    List<ExchangeProductEntity> findAllByExchangeExchangeCode(int exchangeCode);

    List<ExchangeProductEntity> findAllByExchangeExchangeCodeAndExchangeProductStatus(int exchangeCode, EXCHANGE_PRODUCT_STATUS exchangeProductStatus);

    List<ExchangeProductEntity> findByProductProductCodeAndExchangeExchangeCode(int exchangeProductCode, int exchangeCode);

    List<ExchangeProductEntity> findByExchangeExchangeCode(int exchangeCode);
}
