package com.akatsuki.pioms.order.repository;


import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 관리자가 관리하는 모든 가맹점의 발주들을 조회합니다.
    List<Order> findAllByFranchiseAdminAdminCode(int adminId);

    // 관리자가 관리하는 모든 가맹점에 대하여 발주 상태에 따라 조회를 합니다.
    List<Order> findAllByFranchiseAdminAdminCodeAndOrderCondition(int adminId, ORDER_CONDITION orderCondition);

    // 가맹점의 모든 발주들을 조회합니다.
    List<Order> findByFranchiseFranchiseCode(int franchiseCode);
}
