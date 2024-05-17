package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.aggregate.*;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.dto.ExchangeProductDTO;
import com.akatsuki.pioms.exchange.repository.ExchangeProductRepository;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        List<Exchange> exchanges = exchangeRepository.findAll();
        //when
        List<ExchangeDTO> exchangeDTOS = exchangeService.getExchanges();
        //then
        assertEquals(exchanges.size(), exchangeDTOS.size());
    }


    @Test
    void getExchangesByAdminCode() {
        //given
        int adminCode = 1;
        List<Exchange> exchanges = exchangeRepository.findAllByFranchiseAdminAdminCode(adminCode);
        //when
        List<ExchangeDTO> exchangeDTOS = exchangeService.getExchangesByAdminCode(adminCode);
        //then
        assertEquals(exchanges.size(),exchangeDTOS.size());
    }
    @Test
    void putExchange() {
        //given
        int adminCode=1;
        int franchiseCode=1;
        ExchangeDTO exchangeDTO = exchangeService.postExchange(franchiseCode,exchange);
        List<ExchangeProductVO> exchangeProductVOS1 = new ArrayList<>();

        for (ExchangeProductDTO exchangeProduct : exchangeDTO.getExchangeProducts()) {
            exchangeProductVOS1.add(new ExchangeProductVO(
                    exchangeProduct.getExchangeProductCode()
                    , exchangeProduct.getExchangeProductCount()
                    , exchangeProduct.getExchangeProductCount()-1
                    , 1));
        }
        RequestExchange request = new RequestExchange(1,EXCHANGE_STATUS.반송신청,exchangeProductVOS1);

        //when
        ExchangeDTO exchangeDTO1 = exchangeService.putExchange(adminCode,exchangeDTO.getExchangeCode(),request);

        //then
        assertNotEquals(exchangeDTO1,exchangeDTO);
        for (ExchangeProductDTO exchangeProduct : exchangeDTO1.getExchangeProducts()) {
            assertEquals(
                    exchangeProduct.getExchangeProductNormalCount()+ exchangeProduct.getExchangeProductDiscount()
                    , exchangeProduct.getExchangeProductCount() );
        }

    }

    @Test
    void postExchange() {
        //given
        int adminCode=1;
        int franchiseCode=1;
        //when
        ExchangeDTO exchangeDTO = exchangeService.postExchange(franchiseCode,exchange);
        //then
        assertEquals(exchange.getExchangeStatus(), exchangeDTO.getExchangeStatus());
        assertEquals(exchange.getFranchiseCode(), exchangeDTO.getFranchise().getFranchiseCode());
        assertEquals(exchange.getProducts().size(),exchangeDTO.getExchangeProducts().size());
    }

    @Test
    void getExchangeProductsWithStatus() {
        //given
        int adminCode=1;
        int franchiseCode=1;
        ExchangeDTO exchangeDTO = exchangeService.postExchange(franchiseCode,exchange);
        //when
        List<ExchangeProductDTO> exchangeDTOS1 = exchangeService.getExchangeProductsWithStatus(exchangeDTO.getExchangeCode(),EXCHANGE_PRODUCT_STATUS.교환);
        List<ExchangeProductDTO> exchangeDTOS2 = exchangeService.getExchangeProductsWithStatus(exchangeDTO.getExchangeCode(),EXCHANGE_PRODUCT_STATUS.폐기);
        //then
        assertEquals(exchangeDTOS1.size(),2);
        assertEquals(exchangeDTOS2.size(),2);
    }

    @Test
    void getAdminExchange() {
        //given
        int adminCode=1;
        int franchiseCode=1;
        ExchangeDTO exchangeDTO = exchangeService.postExchange(franchiseCode,exchange);

        //when
        ExchangeDTO exchangeDTO1 = exchangeService.getAdminExchange(adminCode,exchangeDTO.getExchangeCode());
        //then
        assertEquals(exchangeDTO1.getExchangeCode(),exchangeDTO.getExchangeCode());
    }

    @Test
    void getFranchiseExchange(){
        //given
        int franchiseCode = 1;
        ExchangeDTO exchangeDTO = exchangeService.postExchange(franchiseCode,exchange);
        int exchangeCode = exchangeDTO.getExchangeCode();
        //when
        ExchangeDTO exchangeDTO1 = exchangeService.getFranchiseExchange(franchiseCode,exchangeCode);
        //then
        assertNotNull(exchangeDTO1);
        assertEquals(exchangeDTO1.getExchangeCode(), exchangeCode);

    }

    @Test
    void getFrOwnerExchanges() {
        //given
        int franchiseOwnerCode= 1;
        int franchisCode = 1;
        List<Exchange> exchanges = exchangeRepository.findAllByFranchiseFranchiseOwnerFranchiseOwnerCode(franchiseOwnerCode);
        //when
        List<ExchangeDTO> exchangeDTOS = exchangeService.getFrOwnerExchanges(franchiseOwnerCode);
        //then
        assertEquals(exchanges.size(), exchangeDTOS.size());
    }


    @Test
    void deleteExchange() {
        //given
        int franchiseOwnerCode= 1;
        int franchiseCode = 1;
        ExchangeDTO exchangeDTO = exchangeService.postExchange(franchiseCode,exchange);
        //when
        exchangeService.deleteExchange(franchiseOwnerCode,exchangeDTO.getExchangeCode());
        //then
        assertNull(exchangeService.getFranchiseExchange(franchiseCode,exchangeDTO.getExchangeCode()));

    }
}