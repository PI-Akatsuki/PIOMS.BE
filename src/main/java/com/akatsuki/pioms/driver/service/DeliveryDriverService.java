package com.akatsuki.pioms.driver.service;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface DeliveryDriverService {

    // 전체 목록 조회
    List<DeliveryDriver> findDriverList();

    // 상세 조회
    Optional<DeliveryDriver> findDriverById(int driverId);

    // 배송기사 등록
    ResponseEntity<String> saveDriver(DeliveryDriver driver, int requestorAdminCode);

    // 배송기사 정보 수정
    ResponseEntity<String> updateDriver(int driverId, DeliveryDriver updatedDriver, Integer requestorAdminCode, Integer requestorDriverCode);
}
