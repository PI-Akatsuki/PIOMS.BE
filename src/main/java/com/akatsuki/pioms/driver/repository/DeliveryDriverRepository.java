package com.akatsuki.pioms.driver.repository;

import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Optional;

public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Integer> {

    // ID로 배송기사 찾기
    Optional<DeliveryDriver> findByDriverId(String driverId);

    List<DeliveryDriver> findAllByOrderByDriverEnrollDateDesc();

}
