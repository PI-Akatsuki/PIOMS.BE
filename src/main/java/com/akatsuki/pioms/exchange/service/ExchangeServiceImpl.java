package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.entity.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService{
    ExchangeRepository exchangeRepository;

    @Autowired
    public ExchangeServiceImpl(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @Override
    public ExchangeDTO findExchangeToSend(int franchiseCode) {
        System.out.println("반품신청 찾기. franchisecode: " + franchiseCode);
        ExchangeEntity exchange = exchangeRepository.findByFranchiseFranchiseCodeAndExchangeStatus(franchiseCode,EXCHANGE_STATUS.반송신청);
        System.out.println("exchange = " + exchange);
        if(exchange==null)
            return null;
        exchange.setExchangeStatus(EXCHANGE_STATUS.반송중);
        exchangeRepository.save(exchange);
        return new ExchangeDTO(exchange);
    }

    //반송 생성

    //가맹점의 반송 대기중인 상품 배송

}
