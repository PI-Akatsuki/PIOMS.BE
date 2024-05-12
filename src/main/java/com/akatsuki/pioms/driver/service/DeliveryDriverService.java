package com.akatsuki.pioms.driver.service;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;

import java.util.List;
import java.util.Optional;

public interface DeliveryDriverService {

    // 전체 목록 조회
    List<DeliveryDriver> findDriverList();

    // 상세 조회
    Optional<DeliveryDriver> findDriverById(int driverId);
}
