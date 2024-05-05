package com.akatsuki.pioms.order.repository;


import com.akatsuki.pioms.order.entity.OrderEntity;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByFranchiseAdminAdminCode(int adminId);
    List<OrderEntity> findAllByFranchiseAdminAdminCodeAndOrderCondition(int adminId, ORDER_CONDITION orderCondition);
}
