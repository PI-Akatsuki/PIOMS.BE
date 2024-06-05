package com.akatsuki.pioms.driver.service;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.dto.DeliveryDriverDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface DeliveryDriverService {

    // 전체 목록 조회
    List<DeliveryDriverDTO> findDriverList();

    // 상세 조회
    Optional<DeliveryDriverDTO> findDriverById(int driverId);

    // 배송기사 등록
    ResponseEntity<String> saveDriver(DeliveryDriverDTO driverDTO);

    // 배송기사 정보 수정
    ResponseEntity<String> updateDriver(int driverId, DeliveryDriverDTO updatedDriverDTO);

    // 배송기사 삭제
    ResponseEntity<String> deleteDriver(int driverId);

    ResponseEntity<String> resetDriverPassword(int driverId);


}
