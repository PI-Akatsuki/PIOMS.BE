package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.exchange.aggregate.*;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.repository.ExchangeProductRepository;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.product.aggregate.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExchangeServiceTest {

    @Autowired
    ExchangeService exchangeService;

    @MockBean
    ExchangeProductRepository exchangeProductRepository;
    @MockBean
    ExchangeRepository exchangeRepository;
    FranchiseOwner franchiseOwner;
    FranchiseOwner franchiseOwner2;
    Admin admin;
    Franchise franchise;
    Franchise franchise2;
    Exchange exchange;
    Exchange exchange2;
    Exchange exchange3;
    List<ExchangeProduct> exchangeProduct;
    List<ExchangeProduct> exchangeProduct2;
    List<ExchangeProduct> exchangeProduct3;

    List<Product> productList;

    @BeforeEach
    void init(){
        franchiseOwner = new FranchiseOwner();
        franchiseOwner.setFranchiseOwnerCode(1);
        franchiseOwner2 = new FranchiseOwner();
        franchiseOwner2.setFranchiseOwnerCode(2);

        admin = new Admin();
        admin.setAdminCode(1);

        franchise = new Franchise(1,"franchise",null,null,null,null,null,null, DELIVERY_DATE.월_목,franchiseOwner,admin);
        franchise2 = new Franchise(2,"franchise",null,null,null,null,null,null, DELIVERY_DATE.월_목,franchiseOwner2,admin);

        exchange = new Exchange(1,null, EXCHANGE_STATUS.반송신청,franchise,null);
        exchange2 = new Exchange(2,null, EXCHANGE_STATUS.반송중,franchise,null);

        exchange3 = new Exchange(2,null, EXCHANGE_STATUS.반송신청,franchise,null);

        productList = Stream.of(
                new Product(1, "물건1",0,null,null,null,null,0,null,1000,null,true,0,0,1000,null),
                new Product(2, "물건2",0,null,null,null,null,0,null,1000,null,true,0,0,1000,null),
                new Product(3, "물건3",0,null,null,null,null,0,null,1000,null,true,0,0,1000,null)
        ).toList();

        exchangeProduct =
                Stream.of(
                        new ExchangeProduct(1, EXCHANGE_PRODUCT_STATUS.교환,20,0,0,productList.get(0),exchange),
                        new ExchangeProduct(2, EXCHANGE_PRODUCT_STATUS.폐기,30,0,0,productList.get(1),exchange),
                        new ExchangeProduct(3, EXCHANGE_PRODUCT_STATUS.폐기,10,0,0,productList.get(2),exchange)
                ).toList();
        exchangeProduct2 =
                Stream.of(
                        new ExchangeProduct(1, EXCHANGE_PRODUCT_STATUS.교환,20,0,0,productList.get(0),exchange2),
                        new ExchangeProduct(2, EXCHANGE_PRODUCT_STATUS.폐기,30,0,0,productList.get(1),exchange2),
                        new ExchangeProduct(3, EXCHANGE_PRODUCT_STATUS.폐기,10,0,0,productList.get(2),exchange2)
                ).toList();
        exchangeProduct3 =
                Stream.of(
                        new ExchangeProduct(1, EXCHANGE_PRODUCT_STATUS.교환,20,0,0,productList.get(0),exchange3),
                        new ExchangeProduct(2, EXCHANGE_PRODUCT_STATUS.폐기,30,0,0,productList.get(1),exchange3),
                        new ExchangeProduct(3, EXCHANGE_PRODUCT_STATUS.폐기,10,0,0,productList.get(2),exchange3)
                ).toList();

        exchange.setProducts(exchangeProduct);
        exchange2.setProducts(exchangeProduct2);
        exchange3.setProducts(exchangeProduct3);
    }

    @Test
    void findExchangeToSend() {
        when(exchangeRepository.findByFranchiseFranchiseCodeAndExchangeStatus(franchise.getFranchiseCode(), EXCHANGE_STATUS.반송신청))
                .thenReturn(
                        exchange
                );
        ExchangeDTO exchangeDTO = exchangeService.findExchangeToSend(franchise.getFranchiseCode());
        assertEquals(exchange.getExchangeCode() , exchangeDTO.getExchangeCode());
    }

    @Test
    void getExchanges() {
        when(exchangeRepository.findAll()).thenReturn(
            Stream.of(
                    exchange,exchange2,exchange3
            ).toList()
        );
        List<ExchangeDTO> exchanges = exchangeService.getExchanges();
        assertEquals(exchanges.size(),3);
    }

    @Test
    void getExchangesByFranchiseCode() {
        //given
        when(exchangeRepository.findAllByFranchiseFranchiseCode(franchise.getFranchiseCode())).thenReturn(
                Stream.of(exchange,exchange2).toList()
        );
        when(exchangeRepository.findAllByFranchiseFranchiseCode(franchise2.getFranchiseCode())).thenReturn(
                Stream.of(exchange3).toList()
        );

        //when
        List<ExchangeDTO> exchangeDTOS = exchangeService.getExchangesByFranchiseCode(franchise.getFranchiseCode());
        List<ExchangeDTO> exchangeDTOS2 = exchangeService.getExchangesByFranchiseCode(franchise2.getFranchiseCode());

        //then
        assertEquals(exchangeDTOS.size(),2);
        assertEquals(exchangeDTOS2.size(),1);

    }

//    @Test
//    void getExchangesByAdminCode() {
//
//    }
//
//    @Test
//    void putExchange() {
//    }
//
//    @Test
//    void postExchange() {
//    }
//
//    @Test
//    void getExchangeProducts() {
//    }
//
//    @Test
//    void getExchangeProductsWithStatus() {
//    }
}