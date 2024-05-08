package com.akatsuki.pioms.order.repository;

import com.akatsuki.pioms.order.entity.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository  extends JpaRepository<OrderProductEntity, Integer> {

}
