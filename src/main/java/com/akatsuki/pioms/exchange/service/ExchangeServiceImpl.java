package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.config.GetUserInfo;
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
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.aggregate.ResponseProduct;
import com.akatsuki.pioms.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExchangeServiceImpl implements ExchangeService{
    private final ExchangeRepository exchangeRepository;
    private final ExchangeProductRepository exchangeProductRepository;
    private final FranchiseWarehouseService franchiseWarehouseService;
    private final OrderService orderService ;
    private final FranchiseService franchiseService;
    private final ProductService productService;
    private final GetUserInfo getUserInfo;

    @Autowired
    public ExchangeServiceImpl(ExchangeRepository exchangeRepository,ExchangeProductRepository exchangeProductRepository,FranchiseWarehouseService franchiseWarehouseService
    , OrderService orderService, FranchiseService franchiseService, ProductService productService,
               GetUserInfo getUserInfo
    ) {
        this.exchangeRepository = exchangeRepository;
        this.exchangeProductRepository = exchangeProductRepository;
        this.franchiseWarehouseService = franchiseWarehouseService;
        this.orderService = orderService;
        this.franchiseService = franchiseService;
        this.productService = productService;
        this.getUserInfo = getUserInfo;
    }


    @Override
    @Transactional
    public ExchangeDTO findExchangeToSend(int franchiseCode) {
        Exchange returnExchange;
        try {
            returnExchange = exchangeRepository.findByFranchiseFranchiseCodeAndExchangeStatus(franchiseCode, EXCHANGE_STATUS.반송신청);
            if (returnExchange==null)
                return null;
            returnExchange.setExchangeStatus(EXCHANGE_STATUS.반송중);
            exchangeRepository.save(returnExchange);
        }
        catch (Exception e){
                returnExchange= null;
        }
        return new ExchangeDTO(returnExchange);
    }

    @Override
    public List<ExchangeDTO> getExchangesByAdminCode() {
        int adminCode=getUserInfo.getAdminCode();
        List<Exchange> exchangeList;
        if(adminCode==1)
            exchangeList = exchangeRepository.findAll();
        else
            exchangeList = exchangeRepository.findAllByFranchiseAdminAdminCodeOrderByExchangeDateDesc(adminCode);

        List<ExchangeDTO> responseList = new ArrayList<>();
        exchangeList.forEach(exchange -> {
            responseList.add(new ExchangeDTO(exchange));
        });
        return responseList;
    }


    @Override
    @Transactional
    public ExchangeDTO postExchange( RequestExchange requestExchange) {
        int franchiseOwnerCode = getUserInfo.getFranchiseOwnerCode();
        FranchiseDTO franchiseDTO = franchiseService.findFranchiseByFranchiseOwnerCode(franchiseOwnerCode);
        if(exchangeRepository.existsByFranchiseFranchiseCodeAndExchangeStatus(franchiseDTO.getFranchiseCode(), EXCHANGE_STATUS.반송신청))
            return null;
        if (franchiseDTO.getFranchiseOwner().getFranchiseOwnerCode()!= franchiseOwnerCode)
            return null;
        if(!franchiseWarehouseService.checkEnableToAddExchangeAndChangeEnableCnt(requestExchange,franchiseDTO.getFranchiseCode()))
            return null;

        Exchange exchange = new Exchange();
        exchange.setExchangeDate(LocalDateTime.now());
        exchange.setExchangeStatus(EXCHANGE_STATUS.반송신청);

        Franchise franchise1 = getFranchise(franchiseOwnerCode, franchiseDTO);

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

    private Franchise getFranchise(int franchiseOwnerCode, FranchiseDTO franchise) {
        Franchise franchise1 = new Franchise();
        franchise1.setFranchiseCode(franchise.getFranchiseCode());
        franchise1.setFranchiseName(franchise.getFranchiseName());
        FranchiseOwner franchiseOwner = new FranchiseOwner();
        franchiseOwner.setFranchiseOwnerCode(franchiseOwnerCode);
        franchiseOwner.setFranchiseOwnerName(franchise.getFranchiseOwner().getFranchiseOwnerName());
        franchise1.setFranchiseOwner(franchiseOwner);
        franchise1.setAdmin(new Admin());
        franchise1.getAdmin().setAdminCode(franchise.getAdminCode());
        return franchise1;
    }

    @Override
    public ExchangeDTO getExchangeByAdminCode( int exchangeCode) {
        int adminCode=getUserInfo.getAdminCode();
        Exchange exchange = exchangeRepository.findById(exchangeCode).orElse(null);
        if (exchange ==null ||exchange.getFranchise().getAdmin().getAdminCode() != adminCode)
            return null;
        return new ExchangeDTO(exchange);
    }

    @Override
    public ExchangeDTO getExchangeByFranchiseOwnerCode( int exchangeCode) {
        int franchiseOwnerCode = getUserInfo.getFranchiseOwnerCode();
        //FIN
        Exchange exchange = exchangeRepository.findById(exchangeCode).orElse(null);
        if (exchange==null|| exchange.getFranchise().getFranchiseOwner().getFranchiseOwnerCode() != franchiseOwnerCode)
            return null;
        return new ExchangeDTO(exchange);
    }

    @Override
    public List<ExchangeDTO> getFrOwnerExchanges() {
        int franchiseOwnerCode = getUserInfo.getFranchiseOwnerCode();
        //FIN
        List<Exchange> exchangeList =
                exchangeRepository.findAllByFranchiseFranchiseOwnerFranchiseOwnerCodeOrderByExchangeDateDesc(franchiseOwnerCode);
        List<ExchangeDTO> exchangeDTOList = new ArrayList<>();
        exchangeList.forEach(exchange -> exchangeDTOList.add(new ExchangeDTO(exchange)));
        return exchangeDTOList;
    }

    @Override
    @Transactional
    public boolean deleteExchange(int exchangeCode) {
        int franchiseOwnerCode = getUserInfo.getFranchiseOwnerCode();
        //FIN
        Exchange exchange = exchangeRepository.findById(exchangeCode).orElse(null);
        if (exchange==null ||
                exchange.getFranchise().getFranchiseOwner().getFranchiseOwnerCode()!= franchiseOwnerCode ||
                orderService.findOrderByExchangeCode(exchangeCode)/*있으면 true로 삭제 불가*/)
            return false;

        for (int i = 0; i <exchange.getProducts().size(); i++) {
            franchiseWarehouseService.saveProductWhenDeleteExchange(
                    exchange.getProducts().get(i).getProduct().getProductCode(),
                    exchange.getProducts().get(i).getExchangeProductCount(),
                    exchange.getFranchise().getFranchiseCode()
            );
        }

        exchangeProductRepository.deleteByExchangeExchangeCode(exchangeCode);
        exchangeRepository.deleteById(exchange.getExchangeCode());
        return true;
    }

    private boolean checkValidationExchangeProducts(List<ExchangeProductVO> products) {
        for (int i = 0; i < products.size(); i++) {
            ExchangeProductVO product = products.get(i);
            int productCode = product.getExchangeProductCode();
            ExchangeProduct exchangeProduct = exchangeProductRepository.findById(productCode).orElse(null);
            if (exchangeProduct==null ||exchangeProduct.getExchangeProductCount() != product.getExchangeProductCount()) {
                return false;
            }
            if (product.getExchangeProductNormalCount() + product.getExchangeProductDiscount() != exchangeProduct.getExchangeProductCount()) {
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

        exchangeProductRepository.save(exchangeProductEntity);
    }



    @Override
    @Transactional
    public void afterAcceptOrder(OrderDTO order) {
        // 주문 승인 후 처리 되어 있는 교환들 반환대기로 변경
        // 본사 재고도 미리 뻄
        List<Exchange> exchanges = exchangeRepository.findAllByFranchiseFranchiseCodeAndExchangeStatus(order.getFranchiseCode(),EXCHANGE_STATUS.처리완료);
        if (exchanges.isEmpty())
            return;
        exchanges.forEach(exchange -> {
            exchange.setExchangeStatus(EXCHANGE_STATUS.반환대기);

            exchangeRepository.save(exchange);
        });
        saveExchangeAndProduct(exchanges);
    }

    private void saveExchangeAndProduct(List<Exchange> exchanges) {
        exchanges.forEach(exchange -> {
            exchange.setExchangeStatus(EXCHANGE_STATUS.반환대기);
            for (int i = 0; i <exchange.getProducts().size(); i++) {
                if (exchange.getProducts().get(i).getExchangeProductStatus() != EXCHANGE_PRODUCT_STATUS.교환)
                    return;
                int productCode= exchange.getProducts().get(i).getProduct().getProductCode();
                int cnt = exchange.getProducts().get(i).getExchangeProductCount();
                try {
                    productService.productMinusCnt(cnt,productCode);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            exchangeRepository.save(exchange);
        });
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
            exchangeRepository.save(exchange);
        });
    }

    @Transactional
    public boolean updateExchangeEndDelivery(int franchiseCode){
        List<Exchange> exchanges = exchangeRepository.findAllByFranchiseFranchiseCodeAndExchangeStatus(franchiseCode,EXCHANGE_STATUS.반환중);
        if (exchanges.isEmpty()){
            return false;
        }
        saveExchangeAndFRWareHouse(franchiseCode, exchanges);
        return true;
    }

    private void saveExchangeAndFRWareHouse(int franchiseCode, List<Exchange> exchanges) {
        exchanges.forEach(exchange -> {
            exchange.setExchangeStatus(EXCHANGE_STATUS.반환완료);
            exchangeRepository.save(exchange);
            for (int i = 0; i < exchange.getProducts().size(); i++) {
                if (exchange.getProducts().get(i).getExchangeProductStatus()== EXCHANGE_PRODUCT_STATUS.교환){
                    franchiseWarehouseService.saveProduct(exchange.getProducts().get(i).getProduct().getProductCode(),
                            exchange.getProducts().get(i).getExchangeProductCount(), franchiseCode);
                }
            }
        });
    }

    @Override
    @Transactional
    public void updateExchangeToCompany(int exchangeCode) {
        // 발주 도착 시 돌려보낼 교환 처리
        // end
        Exchange exchange = exchangeRepository.findById(exchangeCode).orElse(null);
        System.out.println("exchange = " + exchange);
        if (exchange==null){
            return;
        }
        exchange.setExchangeStatus(EXCHANGE_STATUS.처리대기);
        exchange.getProducts().forEach(exchangeProduct ->{
                if(exchangeProduct.getProduct()!=null)
                    franchiseWarehouseService.saveProductWhenUpdateExchangeToCompany(
                        exchangeProduct.getProduct().getProductCode(),
                        exchangeProduct.getExchangeProductCount(),
                        exchange.getFranchise().getFranchiseCode());
                });
        exchangeRepository.save(exchange);
    }

    @Override
    @Transactional
    public ExchangeDTO processArrivedExchange(int exchangeCode, RequestExchange requestExchange) {
        // 관리자가 반품온 상품들 처리하기 위한 메서드
        //
        //FIN
        int adminCode=getUserInfo.getAdminCode();
        Exchange exchangeEntity = exchangeRepository.findById(exchangeCode).orElse(null);
        System.out.println("exchangeEntity = " + exchangeEntity);
        if (exchangeEntity == null){
            System.out.println("없는 교환 코드");
            return null;
        }
        if (exchangeEntity.getExchangeStatus()!=EXCHANGE_STATUS.처리대기){
            System.out.println("exchangeEntity.getExchangeStatus() = " + exchangeEntity.getExchangeStatus());
            return null;
        }
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
        // 해당 교환에 검수 결과 저장
        requestExchange.getProducts().forEach(this::updateExchangeProduct);
        exchangeEntity.setExchangeStatus(EXCHANGE_STATUS.처리완료);
        // 본사 창고 업데이트
        productService.importExchangeProducts(requestExchange);
        exchangeRepository.save(exchangeEntity);
        return new ExchangeDTO(exchangeRepository.findById(exchangeCode).orElseThrow());
    }



    @Override
    public List<ExchangeDTO> findExchangeInDeliveryCompanyToFranchise(int franchiseOwnerCode) {
        List<Exchange> exchanges = exchangeRepository.findAllByExchangeStatusWhenDeliveryCompanyToFranchise(franchiseOwnerCode);
        System.out.println("exchanges = " + exchanges);
        return exchanges.stream().map(ExchangeDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExchangeDTO> getExchangesByAdminCodeOrderByExchangeDateDesc() {
        int adminCode= getUserInfo.getAdminCode();
        List<Exchange> exchanges;
        if (getUserInfo.isRoot(adminCode)) {
            System.out.println("find all exchange");
            exchanges = exchangeRepository.findAllByOrderByExchangeDateDesc();
        }
        else
            exchanges = exchangeRepository.findAllByFranchiseAdminAdminCodeOrderByExchangeDateDesc(adminCode);
        return exchanges.stream().map(ExchangeDTO::new).collect(Collectors.toList());
    }

}
