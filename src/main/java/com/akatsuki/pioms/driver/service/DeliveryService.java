package com.akatsuki.pioms.driver.service;

import com.akatsuki.pioms.driver.aggregate.DeliveryRegion;
import com.akatsuki.pioms.driver.dto.DeliveryRegionDTO;

import java.util.List;
import java.util.Optional;

public interface DeliveryService {

    int getDeliveryRegionCodeByFranchiseCode(int franchiseCode);

    DeliveryRegionDTO getDeliveryRegionByFranchiseCode(int franchiseCode);

    // 배송기사 코드로 배송상태 조회
    List<DeliveryRegion> findAllByDeliveryDriverDriverCode(int driverCode);
}
