package com.akatsuki.pioms.exchange.repository;

import com.akatsuki.pioms.exchange.aggregate.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.aggregate.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange, Integer> {
    Exchange findByFranchiseFranchiseCodeAndExchangeStatus(int franchiseCode, EXCHANGE_STATUS exchangeStatus);

    Exchange findByExchangeStatus(EXCHANGE_STATUS exchangeStatus);

    List<Exchange> findAllByFranchiseFranchiseCodeAndExchangeStatus(int franchiseCode, EXCHANGE_STATUS exchangeStatus);

    void deleteAllByFranchiseFranchiseCodeAndExchangeStatus(int franchiseCode, EXCHANGE_STATUS exchangeStatus);

    List<Exchange> findAllByFranchiseFranchiseCodeAndExchangeStatusOrderByExchangeDateAsc(int franchiseCode, EXCHANGE_STATUS exchangeStatus);

    List<Exchange> findAllByFranchiseFranchiseCode(int franchiseCode);

    List<Exchange> findAllByFranchiseAdminAdminCode(int adminCode);

    List<Exchange> findAllByFranchiseFranchiseOwnerFranchiseOwnerCode(int franchiseOwnerCode);

    boolean existsByFranchiseFranchiseCodeAndExchangeStatus(int franchiseCode, EXCHANGE_STATUS exchangeStatus);


    List<Exchange> findAllByFranchiseAdminAdminCodeOrderByExchangeDateDesc(int adminCode);

    List<Exchange> findAllByFranchiseFranchiseOwnerFranchiseOwnerCodeOrderByExchangeDateDesc(int franchiseOwnerCode);
}
