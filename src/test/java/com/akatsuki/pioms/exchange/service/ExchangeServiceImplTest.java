package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.entity.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExchangeServiceImplTest {

    @Autowired
    ExchangeService exchangeService;
    @Autowired
    ExchangeRepository exchangeRepository;

    @Test
    @DisplayName("test")
    @Transactional
    void Test(){
        ExchangeDTO exchange= exchangeService.findExchangeToSend(1);
        System.out.println("exchange = " + exchange);
    }
    @Test
    @DisplayName("test2")
    void Test2(){

        ExchangeEntity exchange= exchangeRepository.findByExchangeStatus(EXCHANGE_STATUS.반송신청);

        System.out.println("exchange = " + exchange);
    }
}