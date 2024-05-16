package com.akatsuki.pioms.driver.service;

import com.akatsuki.pioms.driver.dto.DeliveryRegionDTO;

public interface DeliveryService {

    int getDeliveryRegionCodeByFranchiseCode(int franchiseCode);

    DeliveryRegionDTO getDeliveryRegionByFranchiseCode(int franchiseCode);
}
