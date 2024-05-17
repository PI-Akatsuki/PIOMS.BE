package com.akatsuki.pioms.driver.service;

import com.akatsuki.pioms.driver.dto.DeliveryRegionDTO;
import com.akatsuki.pioms.driver.aggregate.DeliveryRegion;
import com.akatsuki.pioms.driver.repository.DeliveryRegionRepository;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService{

    private final DeliveryRegionRepository deliveryRegionRepository;

    @Autowired
    public DeliveryServiceImpl(DeliveryRegionRepository deliveryRegionRepository) {
        this.deliveryRegionRepository = deliveryRegionRepository;
    }

    public int getDeliveryRegionCodeByFranchiseCode(int franchiseCode){
        DeliveryRegion deliveryRegion = deliveryRegionRepository.findByFranchiseFranchiseCode(franchiseCode);
        return deliveryRegion.getDeliveryRegionCode();
    }

    @Override
    public DeliveryRegionDTO getDeliveryRegionByFranchiseCode(int franchiseCode) {
        DeliveryRegion deliveryRegion = deliveryRegionRepository.findByFranchiseFranchiseCode(franchiseCode);
        return new DeliveryRegionDTO(deliveryRegion);
    }

    @Override
    public List<DeliveryRegion> findAllByDeliveryDriverDriverCode(int driverCode) {
        return deliveryRegionRepository.findAllByDeliveryDriverDriverCode(driverCode);
    }

}
