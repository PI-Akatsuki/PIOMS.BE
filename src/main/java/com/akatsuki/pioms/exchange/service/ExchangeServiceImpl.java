package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.entity.*;
import com.akatsuki.pioms.exchange.vo.ExchangeProductVO;
import com.akatsuki.pioms.exchange.vo.ResponseExchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.repository.ExchangeProductRepository;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import com.akatsuki.pioms.franchise.entity.FranchiseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Override
    public List<ResponseExchange> getExchanges() {
        List<ExchangeEntity> exchangeEntityList = exchangeRepository.findAll();
        List<ResponseExchange> exchanges = new ArrayList<>();
        exchangeEntityList.forEach(exchangeEntity -> {
            exchanges.add(new ResponseExchange(exchangeEntity));
        });
        return exchanges;
    }

    @Override
    @Transactional
    public ResponseExchange putExchange(int exchangeCode, RequestExchange requestExchange) {
        // 관리자가 반품온 상품들 처리하기 위한 메서드
        ExchangeEntity exchangeEntity = exchangeRepository.findById(exchangeCode).orElseThrow(IllegalArgumentException::new);
        exchangeEntity.setExchangeStatus(requestExchange.getExchangeStatus());
        exchangeRepository.save(exchangeEntity);
        requestExchange.getProducts().forEach(this::updateExchangeProduct);
        return new ResponseExchange(exchangeRepository.findById(exchangeCode).orElseThrow());
    }

    @Override
    @Transactional
    public ResponseExchange postExchange(int franchiseCode, RequestExchange requestExchange) {
        ExchangeEntity exchange = new ExchangeEntity();
        exchange.setExchangeDate(LocalDateTime.now());
        exchange.setExchangeStatus(EXCHANGE_STATUS.반송신청);
        FranchiseEntity franchise = new FranchiseEntity();
        franchise.setFranchiseCode(franchiseCode);
        exchange.setFranchise(franchise);

        ExchangeEntity exchange1 = exchangeRepository.save(exchange);
        exchange1.setProducts(new ArrayList<>());

        requestExchange.getProducts().forEach(product->{
            ExchangeProductEntity exchangeProduct = new ExchangeProductEntity(product);
            exchangeProduct.setExchange(exchange1);
            exchangeProduct= exchangeProductRepository.save(exchangeProduct);
            exchange1.getProducts().add(exchangeProduct);
        });
        return new ResponseExchange(exchange1);

    }

    @Override
    public List<ExchangeProductEntity> getExchangeProducts(int exchangeCode) {
        return exchangeProductRepository.findAllByExchangeExchangeCode(exchangeCode);
    }

    @Override
    public List<ExchangeProductEntity> getExchangeProductsWithStatus(int exchangeCode, EXCHANGE_PRODUCT_STATUS exchangeProductStatus) {
        return exchangeProductRepository.findAllByExchangeExchangeCodeAndExchangeProductStatus(exchangeCode,exchangeProductStatus);
    }


    private void updateExchangeProduct(ExchangeProductVO product) {
        ExchangeProductEntity exchangeProductEntity = exchangeProductRepository.findById(product.getExchangeProductCode()).orElseThrow();
        exchangeProductEntity.setExchangeProductDiscount(product.getExchangeProductDiscount());
        exchangeProductEntity.setExchangeProductDiscount(product.getExchangeProductCode());
        exchangeProductEntity.getProduct().setProductDiscount(
                exchangeProductEntity.getProduct().getProductDiscount()+ product.getExchangeProductDiscount()
        );
        exchangeProductEntity.getProduct().setProductCount(
                exchangeProductEntity.getProduct().getProductCount()+ product.getExchangeProductNormalCount()
        );
    }


}
