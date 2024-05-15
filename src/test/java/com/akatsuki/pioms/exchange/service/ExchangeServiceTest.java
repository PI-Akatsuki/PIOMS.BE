package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.aggregate.*;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.repository.ExchangeProductRepository;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ExchangeServiceTest {

    private ExchangeService exchangeService;
    private ExchangeRepository exchangeRepository;
    private ExchangeProductRepository exchangeProductRepository;

    static List<ExchangeProductVO> exchangeProductVOS;
    static RequestExchange exchange;

    @Autowired
    public ExchangeServiceTest(ExchangeService exchangeService, ExchangeRepository exchangeRepository, ExchangeProductRepository exchangeProductRepository) {
        this.exchangeService = exchangeService;
        this.exchangeRepository = exchangeRepository;
        this.exchangeProductRepository = exchangeProductRepository;
    }

    @BeforeAll
    static void init(){
        // 테스트 할 반송 저장
        exchangeProductVOS = Stream.of(
                new ExchangeProductVO(1, 2,EXCHANGE_PRODUCT_STATUS.교환),
                new ExchangeProductVO(2, 3,EXCHANGE_PRODUCT_STATUS.폐기),
                new ExchangeProductVO(3, 4,EXCHANGE_PRODUCT_STATUS.교환),
                new ExchangeProductVO(4, 5,EXCHANGE_PRODUCT_STATUS.폐기)
        ).toList();
        exchange = new RequestExchange(1,EXCHANGE_STATUS.반송신청,exchangeProductVOS);
    }

    @Test
    void findExchangeToSend() {
        //given
        int franchiseCode = 1;
        ExchangeDTO exchangeDTO = exchangeService.postExchange(franchiseCode,exchange);
        EXCHANGE_STATUS exchangeStatus = EXCHANGE_STATUS.반송신청;
        Exchange repo = exchangeRepository.findByFranchiseFranchiseCodeAndExchangeStatus(franchiseCode,exchangeStatus);

        //when
        ExchangeDTO service = exchangeService.findExchangeToSend(franchiseCode);

        //then
        assertEquals(exchangeDTO.getExchangeCode(), repo.getExchangeCode());
        assertEquals(exchangeDTO.getExchangeCode(), service.getExchangeCode());
    }

    @Test
    void getExchanges() {
        //given
        //when
        //then
    }

    @Test
    void getExchangesByFranchiseCode() {
        //given
        //when
        //then
    }

    @Test
    void getExchangesByAdminCode() {
        //given
        //when
        //then
    }

    @Test
    void putExchange() {
        //given
        //when
        //then
    }

    @Test
    void postExchange() {
        //given
        //when
        //then
    }

    @Test
    void getExchangeProducts() {
        //given
        //when
        //then
    }

    @Test
    void getExchangeProductsWithStatus() {
        //given
        //when
        //then
    }

    @Test
    void getAdminExchange() {
        //given
        //when
        //then
    }

    @Test
    void getFranchiseExchange() {
        //given
        //when
        //then
    }

    @Test
    void getFranchiseExchanges() {
        //given
        //when
        //then
    }

    @Test
    void deleteExchange() {
        //given
        //when
        //then
    }
}