package com.akatsuki.pioms.exchange.repository;

import com.akatsuki.pioms.exchange.aggregate.EXCHANGE_PRODUCT_STATUS;
import com.akatsuki.pioms.exchange.aggregate.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeProductRepository extends JpaRepository<ExchangeProduct, Integer> {

    void deleteAllByExchangeFranchiseFranchiseCodeAndExchangeExchangeStatus(int franchiseCode, EXCHANGE_STATUS exchangeStatus);

    List<ExchangeProduct> findAllByExchangeExchangeCode(int exchangeCode);

    List<ExchangeProduct> findAllByExchangeExchangeCodeAndExchangeProductStatus(int exchangeCode, EXCHANGE_PRODUCT_STATUS exchangeProductStatus);

    List<ExchangeProduct> findByProductProductCodeAndExchangeExchangeCode(int exchangeProductCode, int exchangeCode);

    List<ExchangeProduct> findByExchangeExchangeCode(int exchangeCode);

    void deleteByExchangeExchangeCode(int exchangeCode);
}
