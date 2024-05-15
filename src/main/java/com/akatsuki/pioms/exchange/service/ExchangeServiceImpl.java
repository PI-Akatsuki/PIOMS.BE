package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.exchange.aggregate.*;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProductVO;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.repository.ExchangeProductRepository;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.product.aggregate.Product;
import org.aspectj.lang.annotation.Before;
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
    FranchiseWarehouseService franchiseWarehouseService;
    OrderService orderService ;
    FranchiseService franchiseService;
    @Autowired
    public ExchangeServiceImpl(ExchangeRepository exchangeRepository,ExchangeProductRepository exchangeProductRepository,FranchiseWarehouseService franchiseWarehouseService
    , OrderService orderService, FranchiseService franchiseService) {
        this.exchangeRepository = exchangeRepository;
        this.exchangeProductRepository = exchangeProductRepository;
        this.franchiseWarehouseService = franchiseWarehouseService;
        this.orderService = orderService;
        this.franchiseService = franchiseService;
    }


    @Override
    @Transactional
    public ExchangeDTO findExchangeToSend(int franchiseCode) {

        System.out.println("반품신청 찾기. franchisecode: " + franchiseCode);

        Exchange exchange = null;
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
    public List<ExchangeDTO> getExchanges() {
        List<Exchange> exchangeEntityList = exchangeRepository.findAll();
        List<ExchangeDTO> exchanges = new ArrayList<>();
        exchangeEntityList.forEach(exchangeEntity -> {
            exchanges.add(new ExchangeDTO(exchangeEntity));
        });
        return exchanges;
    }

    @Override
    public List<ExchangeDTO> getExchangesByFranchiseCode(int franchiseCode) {
        List<Exchange> exchangeList =  exchangeRepository.findAllByFranchiseFranchiseCode(franchiseCode);
        List<ExchangeDTO> responseList = new ArrayList<>();
        exchangeList.forEach(exchange -> {
            responseList.add(new ExchangeDTO(exchange));
        });
        return responseList;
    }

    @Override
    public List<ExchangeDTO> getExchangesByAdminCode(int adminCode) {
        List<Exchange> exchangeList = exchangeRepository.findAllByFranchiseAdminAdminCode(adminCode);
        List<ExchangeDTO> responseList = new ArrayList<>();
        exchangeList.forEach(exchange -> {
            responseList.add(new ExchangeDTO(exchange));
        });
        return responseList;
    }


    @Override
    @Transactional
    public ExchangeDTO postExchange(int franchiseOwnerCode, RequestExchange requestExchange) {
        System.out.println("franchiseOwnerCode = " + franchiseOwnerCode);
        System.out.println("requestExchange = " + requestExchange);
        System.out.println("post Exchange 발생");
        if(exchangeRepository.existsByFranchiseFranchiseCodeAndExchangeStatus(requestExchange.getFranchiseCode(), EXCHANGE_STATUS.반송신청))
            return null;
        FranchiseDTO franchise = franchiseService.findFranchiseByFranchiseOwnerCode(franchiseOwnerCode);
        if (franchise.getFranchiseOwner().getFranchiseOwnerCode()!= franchiseOwnerCode)
            return null;
        if(!franchiseWarehouseService.checkEnableToAddExchange(requestExchange))
            return null;
        System.out.println("exchange 저장");


        Exchange exchange = new Exchange();
        exchange.setExchangeDate(LocalDateTime.now());
        exchange.setExchangeStatus(EXCHANGE_STATUS.반송신청);


        Franchise franchise1 = new Franchise();
        franchise1.setFranchiseCode(franchise.getFranchiseCode());
        franchise1.setFranchiseName(franchise.getFranchiseName());

        FranchiseOwner franchiseOwner = new FranchiseOwner();
        franchiseOwner.setFranchiseOwnerCode(franchiseOwnerCode);
        franchiseOwner.setFranchiseOwnerName(franchise.getFranchiseOwner().getFranchiseOwnerName());

        franchise1.setFranchiseOwner(franchiseOwner);
        franchise1.setAdmin(new Admin());
        franchise1.getAdmin().setAdminCode(franchise.getAdminCode());

        exchange.setFranchise(franchise1);

        Exchange exchange1 = exchangeRepository.save(exchange);

        exchange1.setProducts(new ArrayList<>());

        requestExchange.getProducts().forEach(product->{
            ExchangeProduct exchangeProduct = new ExchangeProduct(product);
            exchangeProduct.setExchange(exchange1);
            Product product1 = new Product();
            product1.setProductCode(product.getProductCode());
            exchangeProduct.setProduct(product1);
            exchangeProduct= exchangeProductRepository.save(exchangeProduct);
            exchange1.getProducts().add(exchangeProduct);
        });

        return new ExchangeDTO(exchange1);
    }

    @Override
    public List<ExchangeProduct> getExchangeProducts(int exchangeCode) {
        return exchangeProductRepository.findAllByExchangeExchangeCode(exchangeCode);
    }

    @Override
    public List<ExchangeProduct> getExchangeProductsWithStatus(int exchangeCode, EXCHANGE_PRODUCT_STATUS exchangeProductStatus) {
        return exchangeProductRepository.findAllByExchangeExchangeCodeAndExchangeProductStatus(exchangeCode,exchangeProductStatus);
    }

    @Override
    public ExchangeDTO getAdminExchange(int adminCode,int exchangeCode) {
        Exchange exchange = exchangeRepository.findById(exchangeCode).orElse(null);
        if (exchange.getFranchise().getAdmin().getAdminCode() != adminCode)
            return null;
        return new ExchangeDTO(exchange);
    }

    @Override
    public ExchangeDTO getFranchiseExchange(int franchiseOwnerCode,int exchangeCode) {
        //FIN
        Exchange exchange = exchangeRepository.findById(exchangeCode).orElse(null);
        if (exchange.getFranchise().getFranchiseOwner().getFranchiseOwnerCode() != franchiseOwnerCode)
            return null;
        return new ExchangeDTO(exchange);
    }

    @Override
    public List<ExchangeDTO> getFranchiseExchanges(int franchiseOwnerCode) {
        //FIN
        List<Exchange> exchangeList = exchangeRepository.findAllByFranchiseFranchiseOwnerFranchiseOwnerCode(franchiseOwnerCode);
        List<ExchangeDTO> exchangeDTOList = new ArrayList<>();
        exchangeList.forEach(exchange -> exchangeDTOList.add(new ExchangeDTO(exchange)));
        return exchangeDTOList;
    }

    @Override
    @Transactional
    public boolean deleteExchange(int franchiseOwnerCode, int exchangeCode) {
        //FIN
        Exchange exchange = exchangeRepository.findById(exchangeCode).orElse(null);

        if (exchange==null || exchange.getFranchise().getFranchiseOwner().getFranchiseOwnerCode() != franchiseOwnerCode
        || orderService.findOrderByExchangeCode(exchangeCode)/*있으면 true로 삭제 불가*/)
            return false;
        System.out.println("delete");
        exchangeProductRepository.deleteByExchangeExchangeCode(exchangeCode);
        exchangeRepository.delete(exchange);
        return true;
    }

    @Override
    @Transactional
    public ExchangeDTO putExchange(int adminCode,int exchangeCode, RequestExchange requestExchange) {
        // 관리자가 반품온 상품들 처리하기 위한 메서드
        //FIN
        Exchange exchangeEntity = exchangeRepository.findById(exchangeCode).orElseThrow(IllegalArgumentException::new);
        if (exchangeEntity.getFranchise().getAdmin().getAdminCode() != adminCode) {
            System.out.println("1차 검증 실패: 관리자 접근 권한이 없습니다." );
            return null;
        }
        if (requestExchange.getProducts().size() != exchangeEntity.getProducts().size()) {
            System.out.println("2차 검증 실패: 반송 상품 갯수 불일치" );
            System.out.println("좌: "+ requestExchange.getProducts().size() + " 우: "+exchangeEntity.getProducts().size() );
            return null;
        }
        if (!checkValidationExchangeProducts(requestExchange.getProducts())){
            System.out.println("3차 검증 실패: 반송 상품 검수 갯수 불일치" );
            return null;
        }
        System.out.println("exchangeEntity = " + exchangeEntity);
        requestExchange.getProducts().forEach(this::updateExchangeProduct);
        exchangeEntity.setExchangeStatus(requestExchange.getExchangeStatus());
        exchangeRepository.save(exchangeEntity);
        return new ExchangeDTO(exchangeRepository.findById(exchangeCode).orElseThrow());
    }

    private boolean checkValidationExchangeProducts(List<ExchangeProductVO> products) {
        for (int i = 0; i < products.size(); i++) {
            ExchangeProductVO product = products.get(i);
            int productCode = product.getExchangeProductCode();
            ExchangeProduct exchangeProduct = exchangeProductRepository.findById(productCode).orElseThrow();
            if (exchangeProduct.getExchangeProductCount() != product.getExchangeProductCount()) {
                System.out.println(productCode + " 번 반송상품 코드 문제 발생! ExchangeProductCount 불일치!");
                return false;
            }
            if (product.getExchangeProductNormalCount() + product.getExchangeProductDiscount() != exchangeProduct.getExchangeProductCount()) {
                System.out.println(productCode + " 번 반송상품 코드 문제 발생! 반품 검수 합 불일치!");
                return false;
            }
        }

        return true;
    }


    private void updateExchangeProduct(ExchangeProductVO product) {
        //FIN
        ExchangeProduct exchangeProductEntity =
                exchangeProductRepository.findById(product.getExchangeProductCode()).orElseThrow();

        //해당 상품 처리
        exchangeProductEntity.setExchangeProductNormalCount(product.getExchangeProductNormalCount());
        exchangeProductEntity.setExchangeProductDiscount(product.getExchangeProductDiscount());

        //본사 창고 저장
        exchangeProductEntity.getProduct().setProductDiscount(
                exchangeProductEntity.getProduct().getProductDiscount()+ product.getExchangeProductDiscount()
        );
        exchangeProductEntity.getProduct().setProductCount(
                exchangeProductEntity.getProduct().getProductCount()+ product.getExchangeProductNormalCount()
        );
        exchangeProductRepository.save(exchangeProductEntity);
    }


}
