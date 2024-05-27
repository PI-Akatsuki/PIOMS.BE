package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.exchange.aggregate.*;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProductVO;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.dto.ExchangeProductDTO;
import com.akatsuki.pioms.exchange.repository.ExchangeProductRepository;
import com.akatsuki.pioms.exchange.repository.ExchangeRepository;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.service.ProductService;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService{
    private final ExchangeRepository exchangeRepository;
    private final ExchangeProductRepository exchangeProductRepository;
    private final FranchiseWarehouseService franchiseWarehouseService;
    private final OrderService orderService ;
    private final FranchiseService franchiseService;
//    private final ProductService productService;

    @Autowired
    public ExchangeServiceImpl(ExchangeRepository exchangeRepository,ExchangeProductRepository exchangeProductRepository,FranchiseWarehouseService franchiseWarehouseService
    , OrderService orderService, FranchiseService franchiseService
//            , ProductService productService
    ) {
        this.exchangeRepository = exchangeRepository;
        this.exchangeProductRepository = exchangeProductRepository;
        this.franchiseWarehouseService = franchiseWarehouseService;
        this.orderService = orderService;
        this.franchiseService = franchiseService;
//        this.productService = productService;
    }


    @Override
    @Transactional
    public ExchangeDTO findExchangeToSend(int franchiseCode) {
        List<Exchange> exchange = null;
        exchange = exchangeRepository.findAllByFranchiseFranchiseCodeAndExchangeStatus(franchiseCode, EXCHANGE_STATUS.반송신청);
        if(exchange.isEmpty())
            return null;
        Exchange returnExchange = exchange.get(0);

        returnExchange.setExchangeStatus(EXCHANGE_STATUS.반송중);
        exchangeRepository.save(returnExchange);
        return new ExchangeDTO(returnExchange);
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
        if(exchangeRepository.existsByFranchiseFranchiseCodeAndExchangeStatus(requestExchange.getFranchiseCode(), EXCHANGE_STATUS.반송신청))
            return null;
        FranchiseDTO franchise = franchiseService.findFranchiseByFranchiseOwnerCode(franchiseOwnerCode);
        if (franchise.getFranchiseOwner().getFranchiseOwnerCode()!= franchiseOwnerCode)
            return null;
        if(!franchiseWarehouseService.checkEnableToAddExchange(requestExchange))
            return null;

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

        Exchange exchange2 = exchangeRepository.findById(exchange1.getExchangeCode()).orElseThrow();
        System.out.println("exchange2 = " + exchange2);
        return new ExchangeDTO(exchange2);
    }

    @Override
    public List<ExchangeProductDTO> getExchangeProductsWithStatus(int exchangeCode, EXCHANGE_PRODUCT_STATUS exchangeProductStatus) {
        List<ExchangeProduct> exchangeProducts =exchangeProductRepository.findAllByExchangeExchangeCodeAndExchangeProductStatus(exchangeCode,exchangeProductStatus);
        List<ExchangeProductDTO> exchangeProductDTOS = new ArrayList<>();
        for (ExchangeProduct exchangeProduct : exchangeProducts) {
            exchangeProductDTOS.add(new ExchangeProductDTO(exchangeProduct));
        }
        return exchangeProductDTOS;
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
        if (exchange==null|| exchange.getFranchise().getFranchiseOwner().getFranchiseOwnerCode() != franchiseOwnerCode)
            return null;
        return new ExchangeDTO(exchange);
    }

    @Override
    public List<ExchangeDTO> getFrOwnerExchanges(int franchiseOwnerCode) {
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
        System.out.println("exchange = " + exchange);
        if (exchange==null || exchange.getFranchise().getFranchiseOwner().getFranchiseOwnerCode() != franchiseOwnerCode
        || orderService.findOrderByExchangeCode(exchangeCode)/*있으면 true로 삭제 불가*/)
            return false;
        System.out.println("delete");
        for (int i = 0; i <exchange.getProducts().size(); i++) {
            franchiseWarehouseService.saveProductWhenDeleteExchange(
                    exchange.getProducts().get(i).getProduct().getProductCode(),
                    exchange.getProducts().get(i).getExchangeProductCount(),
                    exchange.getFranchise().getFranchiseCode()
            );
        }
        exchangeProductRepository.deleteByExchangeExchangeCode(exchangeCode);
        exchangeRepository.delete(exchange);
        return true;
    }

    private boolean checkValidationExchangeProducts(List<ExchangeProductVO> products) {
        for (int i = 0; i < products.size(); i++) {
            ExchangeProductVO product = products.get(i);
            int productCode = product.getExchangeProductCode();
            System.out.println("productCode = " + productCode);
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

    @Transactional
    public void updateExchangeProduct(ExchangeProductVO product) {
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

    @Transactional
    public boolean updateExchangeEndDelivery(int franchiseCode){
        List<Exchange> exchanges = exchangeRepository.findAllByFranchiseFranchiseCodeAndExchangeStatus(franchiseCode,EXCHANGE_STATUS.반환중);
        if (exchanges.isEmpty()){
            return false;
        }
        exchanges.forEach(exchange -> {
            exchange.setExchangeStatus(EXCHANGE_STATUS.반환완료);
            exchangeRepository.save(exchange);
            for (int i = 0; i < exchange.getProducts().size(); i++) {
                if (exchange.getProducts().get(i).getExchangeProductStatus()== EXCHANGE_PRODUCT_STATUS.교환){
                    franchiseWarehouseService.saveProduct(exchange.getProducts().get(i).getProduct().getProductCode(),
                            exchange.getProducts().get(i).getExchangeProductCount(),franchiseCode);
                }
            }
        });

        return true;
    }

    @Override
    @Transactional
    public void updateExchangeStartDelivery(int franchiseCode) {
        // 배송 시작하게 되면 이 떄 반환대기로 있는 교환품목 같이 배송
        List<Exchange> exchanges = exchangeRepository.findAllByFranchiseFranchiseCodeAndExchangeStatus(franchiseCode,EXCHANGE_STATUS.반환대기);

        if (exchanges.isEmpty())
            return;
        exchanges.forEach(exchange -> {
            exchange.setExchangeStatus(EXCHANGE_STATUS.반환중);
            exchange.getProducts().forEach( exchangeProduct -> {
                if(exchangeProduct.getExchangeProductStatus() == EXCHANGE_PRODUCT_STATUS.교환)
                    franchiseWarehouseService.saveProduct(
                            exchangeProduct.getProduct().getProductCode(),
                            exchangeProduct.getExchangeProductCount(),
                            franchiseCode);
                }
            );
            exchangeRepository.save(exchange);
        });
    }

    @Override
    @Transactional
    public void updateExchangeToCompany(int exchangeCode) {
        Exchange exchange = exchangeRepository.findById(exchangeCode).orElse(null);
        if (exchange==null){
            return;
        }
        exchange.setExchangeStatus(EXCHANGE_STATUS.처리대기);
        exchangeRepository.save(exchange);
    }

    @Override
    @Transactional
    public ExchangeDTO processArrivedExchange(int adminCode, int exchangeCode, RequestExchange requestExchange) {
        // 관리자가 반품온 상품들 처리하기 위한 메서드
        //FIN
        Exchange exchangeEntity = exchangeRepository.findById(exchangeCode).orElseThrow(IllegalArgumentException::new);
        if (exchangeEntity.getFranchise().getAdmin().getAdminCode() != adminCode && adminCode !=1) {
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
        exchangeEntity.setExchangeStatus(EXCHANGE_STATUS.처리완료);
        exchangeRepository.save(exchangeEntity);
        return new ExchangeDTO(exchangeRepository.findById(exchangeCode).orElseThrow());
    }

    @Override
    @Transactional
    public void afterAcceptOrder(OrderDTO order) {

        // 주문 후 처리 되어 있는 교환들 반환대기로 변경
        // 수량은 배송 출발시 변경 될 예정
        List<Exchange> exchanges = exchangeRepository.findAllByFranchiseFranchiseCodeAndExchangeStatus(order.getFranchiseCode(),EXCHANGE_STATUS.처리완료);
        if (exchanges.isEmpty())
            return;
        exchanges.forEach(exchange -> {
            exchange.setExchangeStatus(EXCHANGE_STATUS.반환대기);

            exchangeRepository.save(exchange);
        });
    }
}
