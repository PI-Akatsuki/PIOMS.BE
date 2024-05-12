package com.akatsuki.pioms.driver.repository;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Integer> {
}
