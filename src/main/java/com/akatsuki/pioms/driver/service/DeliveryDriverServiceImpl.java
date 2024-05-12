package com.akatsuki.pioms.driver.service;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryDriverServiceImpl implements DeliveryDriverService{

    private final DeliveryDriverRepository deliveryDriverRepository;

    @Autowired
    public DeliveryDriverServiceImpl(DeliveryDriverRepository deliveryDriverRepository) {
        this.deliveryDriverRepository = deliveryDriverRepository;
    }


    // 전체 조회
    @Override
    public List<DeliveryDriver> findDriverList() {
        return deliveryDriverRepository.findAll();
    }

    // 상세 조회
    @Transactional(readOnly = true)
    @Override
    public Optional<DeliveryDriver> findDriverById(int driverId) {
        return deliveryDriverRepository.findById(driverId);
    }

}
