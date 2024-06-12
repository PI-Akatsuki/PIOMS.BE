package com.akatsuki.pioms.frwarehouse.service;

import com.akatsuki.pioms.admin.repository.AdminRepository;
import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.config.KakaoProperties;
import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.exchange.aggregate.ExchangeProductVO;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.repository.FranchiseRepository;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.frwarehouse.aggregate.FranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.aggregate.RequestFranchiseWarehouse;
import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
import com.akatsuki.pioms.frwarehouse.repository.FranchiseWarehouseRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FranchiseWarehouseServiceImpl implements FranchiseWarehouseService {
    private final FranchiseWarehouseRepository franchiseWarehouseRepository;
    private final AdminRepository adminRepository;
    private final FranchiseService franchiseService;
    private final GetUserInfo getUserInfo;
    private final FranchiseRepository franchiseRepository;
    private final KakaoProperties kakaoProperties;
    private final FranchiseOwnerRepository franchiseOwnerRepository;

    @Autowired
    public FranchiseWarehouseServiceImpl(FranchiseWarehouseRepository franchiseWarehouseRepository, AdminRepository adminRepository,
                                         FranchiseService franchiseService, GetUserInfo getUserInfo,
                                         FranchiseRepository franchiseRepository, KakaoProperties kakaoProperties,
                                         FranchiseOwnerRepository franchiseOwnerRepository ) {
        this.franchiseWarehouseRepository = franchiseWarehouseRepository;
        this.adminRepository = adminRepository;
        this.franchiseService = franchiseService;
        this.getUserInfo = getUserInfo;
        this.franchiseRepository = franchiseRepository;
        this.kakaoProperties = kakaoProperties;
        this.franchiseOwnerRepository = franchiseOwnerRepository;
    }

    @Transactional
    @Override
    public void saveProduct(int productCode, int changeVal, int franchiseCode) {
        System.out.println("가맹창고접근");
        FranchiseWarehouse franchiseWarehouse
                = franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(productCode, franchiseCode);
        System.out.println("franchiseWarehouse = " + franchiseWarehouse);
        //없다면 새로 저장하긔
        if (franchiseWarehouse == null) {
            franchiseWarehouse = new FranchiseWarehouse(false, franchiseCode, productCode);
        }
        if (changeVal >= 0)
            franchiseWarehouse.setFranchiseWarehouseTotal(franchiseWarehouse.getFranchiseWarehouseTotal() + changeVal);

        int oldCount = franchiseWarehouse.getFranchiseWarehouseCount();
        franchiseWarehouse.setFranchiseWarehouseCount(franchiseWarehouse.getFranchiseWarehouseCount() + changeVal);
        franchiseWarehouse.setFranchiseWarehouseEnable(franchiseWarehouse.getFranchiseWarehouseEnable() + changeVal);
        franchiseWarehouseRepository.save(franchiseWarehouse);
        System.out.println("saved");

        // 재고가 5개 이하로 떨어질 때 알림톡 전송
        int threshold = 5;
        if (franchiseWarehouse.getFranchiseWarehouseEnable() <= threshold && oldCount > threshold) {
            try {
                sendKakaoAlert(franchiseWarehouse.getProduct().getProductName(), franchiseWarehouse.getFranchiseWarehouseEnable());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
    @Override
    public void saveProductWhenDeleteExchange(int productCode, int changeVal, int franchiseCode) {
        FranchiseWarehouse franchiseWarehouse
                = franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(productCode, franchiseCode);
        franchiseWarehouse.setFranchiseWarehouseEnable(franchiseWarehouse.getFranchiseWarehouseEnable() + changeVal);
        franchiseWarehouseRepository.save(franchiseWarehouse);
    }

    @Override
    public void saveProductWhenUpdateExchangeToCompany(int productCode, int changeVal, int franchiseCode) {
        FranchiseWarehouse franchiseWarehouse
                = franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(productCode, franchiseCode);

        franchiseWarehouse.setFranchiseWarehouseCount(franchiseWarehouse.getFranchiseWarehouseCount() - changeVal);
        franchiseWarehouse = franchiseWarehouseRepository.save(franchiseWarehouse);
        System.out.println("franchiseWarehouse = " + franchiseWarehouse.getFranchiseWarehouseCount());
        System.out.println("franchiseWarehouse = " + franchiseWarehouse.getFranchiseWarehouseEnable());
        // 재고가 5개 이하로 떨어질 때 알림톡 전송
        int threshold = 5;
        if (franchiseWarehouse.getFranchiseWarehouseEnable() <= threshold && franchiseWarehouse.getFranchiseWarehouseCount() <= threshold) {
            try {
                System.out.println("가맹 창고에 재고가 5개 이하이기 떄문에 알람을 보냅니다." );
                sendKakaoAlert(franchiseWarehouse.getProduct().getProductName(), franchiseWarehouse.getFranchiseWarehouseEnable());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    @Transactional
    public boolean checkEnableToAddExchangeAndChangeEnableCnt(RequestExchange requestExchange, int franchiseCode) {

        for (int i = 0; i < requestExchange.getProducts().size(); i++) {
            ExchangeProductVO exchange = requestExchange.getProducts().get(i);
            FranchiseWarehouse franchiseWarehouse =
                    franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(exchange.getProductCode(), franchiseCode);
            if (franchiseWarehouse == null || franchiseWarehouse.getFranchiseWarehouseEnable() < exchange.getExchangeProductCount()) {
                return false;
            }
        }
        editCountByPostExchange(requestExchange, franchiseCode);
        return true;
    }

    @Transactional
    public void editCountByPostExchange(RequestExchange requestExchange, int franchiseCode) {
        int cnt = requestExchange.getProducts().size();
        for (int i = 0; i < cnt; i++) {
            ExchangeProductVO exchange = requestExchange.getProducts().get(i);
            FranchiseWarehouse franchiseWarehouse =
                    franchiseWarehouseRepository.findByProductProductCodeAndFranchiseCode(exchange.getProductCode(), franchiseCode);
            System.out.println(franchiseWarehouse);
            if (franchiseWarehouse != null) {
                franchiseWarehouse.setFranchiseWarehouseEnable(franchiseWarehouse.getFranchiseWarehouseEnable() - exchange.getExchangeProductCount());
                System.out.println("sadfjfsddfbshsdfbj");
                System.out.println("franchiseWarehouse = " + franchiseWarehouse);
                franchiseWarehouseRepository.save(franchiseWarehouse);
            }
        }
    }

    @Override
    @Transactional
    public List<FranchiseWarehouseDTO> getAllWarehouse() {
        List<FranchiseWarehouse> franchiseWarehouseList = franchiseWarehouseRepository.findAll();
        List<FranchiseWarehouseDTO> responseFrWarehouse = new ArrayList<>();

        franchiseWarehouseList.forEach(franchiseWarehouse -> {
            responseFrWarehouse.add(new FranchiseWarehouseDTO(franchiseWarehouse));
        });
        return responseFrWarehouse;
    }

    @Override
    @Transactional
    public List<FranchiseWarehouseDTO> getWarehouseByWarehouseCode(int franchiseWarehouseCode) {
        List<FranchiseWarehouse> franchiseWarehouseList = franchiseWarehouseRepository.findByFranchiseWarehouseCode(franchiseWarehouseCode);
        List<FranchiseWarehouseDTO> franchiseWarehouseDTOS = new ArrayList<>();
        franchiseWarehouseList.forEach(franchiseWarehouse -> {
            franchiseWarehouseDTOS.add(new FranchiseWarehouseDTO(franchiseWarehouse));
        });
        return franchiseWarehouseDTOS;
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateWarehouseCount(int franchiseWarehouseCode, RequestFranchiseWarehouse request) {
        FranchiseWarehouse franchiseWarehouse = franchiseWarehouseRepository.findById(franchiseWarehouseCode)
                .orElseThrow(() -> new EntityNotFoundException("FranchiseWarehouse not found"));

        int oldCount = franchiseWarehouse.getFranchiseWarehouseCount();
        franchiseWarehouse.setFranchiseWarehouseTotal(request.getFranchiseWarehouseTotal());
        franchiseWarehouse.setFranchiseWarehouseEnable(request.getFranchiseWarehouseEnable());
        franchiseWarehouse.setFranchiseWarehouseCount(request.getFranchiseWarehouseCount());

        franchiseWarehouseRepository.save(franchiseWarehouse);

        // 재고가 5개 이하로 떨어질 때 알림톡 전송
        int threshold = 5;
        if (franchiseWarehouse.getFranchiseWarehouseEnable() <= threshold && oldCount > threshold) {
            try {
                sendKakaoAlert(franchiseWarehouse.getProduct().getProductName(), franchiseWarehouse.getFranchiseWarehouseEnable());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.ok("재고 수정 완료!");
    }

    @Override
    public List<FranchiseWarehouseDTO> getFrWarehouseList() {

//        int franchiseOwnerCode = convertUser.getCode();
        int franchiseOwnerCode = getUserInfo.getFranchiseOwnerCode();

        int franchiseCode = franchiseService.findFranchiseByFranchiseOwnerCode(franchiseOwnerCode).getFranchiseCode();
        List<FranchiseWarehouse> franchiseWarehouses = franchiseWarehouseRepository.findAllByFranchiseCode(franchiseCode);
        List<FranchiseWarehouseDTO> franchiseWarehouseDTOS = new ArrayList<>();
        for (int i = 0; i < franchiseWarehouses.size(); i++) {
            franchiseWarehouseDTOS.add(new FranchiseWarehouseDTO(franchiseWarehouses.get(i)));
        }
        return franchiseWarehouseDTOS;
    }

    @Override
    @Transactional
    public void toggleFavorite(int franchiseWarehouseCode) {
        // 로그 추가: 메서드 시작 및 입력 값 확인
        System.out.println("toggleFavorite 메서드 시작: franchiseWarehouseCode = " + franchiseWarehouseCode);

        // findById 호출 및 결과 확인
        FranchiseWarehouse favorite = franchiseWarehouseRepository.findById(franchiseWarehouseCode)
                .orElseThrow(() -> {
                    // 로그 추가: Warehouse not found
                    System.out.println("Warehouse not found for franchiseWarehouseCode = " + franchiseWarehouseCode);
                    return new RuntimeException("Warehouse not found");
                });

        // 로그 추가: 현재 즐겨찾기 상태 확인
        System.out.println("현재 즐겨찾기 상태: " + favorite.isFranchiseWarehouseFavorite());

        if (favorite.isFranchiseWarehouseFavorite()) {
            // 로그 추가: 이미 즐겨찾기 상태일 때
            System.out.println("이미 즐겨찾기 추가된 상품입니다");
            throw new RuntimeException("이미 즐겨찾기 추가된 상품입니다");
        }

        // 즐겨찾기 상태 변경
        favorite.setFranchiseWarehouseFavorite(true);

        // 로그 추가: 즐겨찾기 상태 변경 후 확인
        System.out.println("즐겨찾기 상태 변경 후: " + favorite.isFranchiseWarehouseFavorite());

        // 데이터베이스에 저장
        franchiseWarehouseRepository.save(favorite);

        // 로그 추가: 저장 완료
        System.out.println("즐겨찾기 상태 저장 완료");
    }

    @Transactional
    public void removeFavorite(int franchiseWarehouseCode) {
        FranchiseWarehouse favorite = franchiseWarehouseRepository.findById(franchiseWarehouseCode)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        if (!favorite.isFranchiseWarehouseFavorite()) {
            throw new RuntimeException("즐겨찾기 추가되지 않은 상품입니다");
        }

        favorite.setFranchiseWarehouseFavorite(false);
        franchiseWarehouseRepository.save(favorite);
    }

    @Override
    public List<FranchiseWarehouse> findAllFavorites() {
        return franchiseWarehouseRepository.findByFranchiseWarehouseFavoriteTrue();
    }

    @Override
    public List<FranchiseWarehouseDTO> getProductsByFranchiseOwnerCode(int franchiseOwnerCode) {
        // FranchiseOwnerCode로 FranchiseCode 가져오기
        Franchise franchise = franchiseRepository.findByFranchiseOwnerFranchiseOwnerCode(franchiseOwnerCode);
        if (franchise == null) {
            throw new RuntimeException("Franchise not found for franchise owner code: " + franchiseOwnerCode);
        }
        int franchiseCode = franchise.getFranchiseCode();

        // FranchiseCode로 FranchiseWarehouseDTO 리스트 가져오기
        List<FranchiseWarehouse> franchiseWarehouses = franchiseWarehouseRepository.findByFranchiseCode(franchiseCode);
        return franchiseWarehouses.stream()
                .map(FranchiseWarehouseDTO::new)
                .collect(Collectors.toList());
    }


    public void sendKakaoAlert(String productName, int stockQuantity) throws JsonProcessingException {
        String url = kakaoProperties.getUrl() + "/v2/api/talk/memo/default/send";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(kakaoProperties.getToken()); // OAuth 토큰 추가

        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", productName + "의 재고가 " + stockQuantity + "개 남았습니다.");
        Map<String, String> linkObject = new HashMap<>();
        linkObject.put("web_url", "http://pioms.shop");
        linkObject.put("mobile_web_url", "http://pioms.shop");
        templateObject.put("link", linkObject);
        templateObject.put("button_title", "바로 확인");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonTemplateObject = objectMapper.writeValueAsString(templateObject);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("template_object", jsonTemplateObject);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            // sout
            System.out.println("Sending Kakao alert with the following details:");
            System.out.println("URL: " + url);
            System.out.println("Headers: " + headers);
            System.out.println("Request Body: " + requestBody);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("알림톡 전송 성공");
            } else {
                System.out.println("알림톡 전송 실패: " + response.getBody());
            }
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<FranchiseWarehouseDTO> findFavoritesByOwner(int franchiseOwnerCode) {
        int franchiseCode = franchiseOwnerRepository.findById(franchiseOwnerCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid franchise owner code"))
                .getFranchise().getFranchiseCode();
        return franchiseWarehouseRepository.findByFranchiseCodeAndFranchiseWarehouseFavorite(franchiseCode, true).stream()
                .map(FranchiseWarehouseDTO::new)
                .collect(Collectors.toList());
    }
}
