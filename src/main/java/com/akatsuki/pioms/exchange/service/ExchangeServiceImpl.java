package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.entity.EXCHANGE_STATUS;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.exchange.repository.ExchangeProductRepository;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService{
    ExchangeRepository exchangeRepository;
    ExchangeProductRepository exchangeProductRepository;
    @Autowired
    public ExchangeServiceImpl(ExchangeRepository exchangeRepository,ExchangeProductRepository exchangeProductRepository) {
        this.exchangeRepository = exchangeRepository;
        this.exchangeProductRepository = exchangeProductRepository;
    }

    @Override
    @Transactional
    public ExchangeDTO findExchangeToSend(int franchiseCode) {
        System.out.println("반품신청 찾기. franchisecode: " + franchiseCode);
        ExchangeEntity exchange = null;
        try {
            exchange = exchangeRepository.findByFranchiseFranchiseCodeAndExchangeStatus(franchiseCode, EXCHANGE_STATUS.반송신청);
            System.out.println("exchange = " + exchange);
        }catch (Exception e){
            System.out.println("처리 할 반품 요소가 많음! 이에 관련된 가장 오래된 데이터를 제외한 모든 반품 삭제 실행");
            exchangeProductRepository.deleteAllByExchangeFranchiseFranchiseCodeAndExchangeExchangeStatus(franchiseCode,EXCHANGE_STATUS.반송신청);
            exchangeRepository.deleteAllByFranchiseFranchiseCodeAndExchangeStatus(franchiseCode,EXCHANGE_STATUS.반송신청);
        }
        if(exchange==null)
            return null;
        exchange.setExchangeStatus(EXCHANGE_STATUS.반송중);
        exchangeRepository.save(exchange);
        return new ExchangeDTO(exchange);
    }

    //반송 생성

    //가맹점의 반송 대기중인 상품 배송

}
