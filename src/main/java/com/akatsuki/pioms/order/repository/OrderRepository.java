package com.akatsuki.pioms.order.repository;


import com.akatsuki.pioms.order.entity.Order;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByFranchiseAdminAdminCode(int adminId);
    List<Order> findAllByFranchiseAdminAdminCodeAndOrderCondition(int adminId, ORDER_CONDITION orderCondition);

    List<Order> findByFranchiseFranchiseCode(int franchiseCode);
}
