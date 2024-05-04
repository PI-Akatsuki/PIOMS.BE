package com.akatsuki.pioms.order.repository;


import com.akatsuki.pioms.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByFranchiseCodeAdminCodeOrderByOrderDateDesc();

}
