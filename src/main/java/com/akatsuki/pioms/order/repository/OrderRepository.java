package com.akatsuki.pioms.order.repository;


import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // 관리자가 관리하는 모든 가맹점의 발주들을 조회합니다.
    List<Order> findAllByFranchiseAdminAdminCodeOrderByOrderDateDesc(int adminId);

    // 관리자가 관리하는 모든 가맹점에 대하여 발주 상태에 따라 조회를 합니다.
    List<Order> findAllByFranchiseAdminAdminCodeAndOrderConditionOrderByOrderDateDesc(int adminCode, ORDER_CONDITION orderCondition);

    // 가맹점의 모든 발주들을 조회합니다.
    List<Order> findByFranchiseFranchiseCodeOrderByOrderDateDesc(int franchiseCode);

    boolean existsByFranchiseFranchiseCodeAndOrderCondition(int franchiseCode, ORDER_CONDITION orderCondition);

    boolean existsByExchangeExchangeCode(int exchangeCode);

    List<Order> findAllByOrderConditionOrderByOrderDateDesc(ORDER_CONDITION orderCondition);

    Order findByFranchiseFranchiseCodeAndOrderCondition(int adminCode, ORDER_CONDITION orderCondition);

    List<Order> findByFranchiseFranchiseOwnerFranchiseOwnerCodeOrderByOrderDateDesc(int franchiseCode);


    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='승인대기' ")
    int getWaitCnt();
    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='승인완료' ")
    int getAcceptCnt();
    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='승인거부' ")
    int getDenyCnt();
    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='배송중' ")
    int getDeliveryCnt();
    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='검수대기' ")
    int getInspectionWaitCnt();
    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='검수완료' ")
    int getInspectionFinishCnt();

    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='승인대기' AND a.orderCode = :adminCode ")
    int getWaitCntByAdminCode(@Param("adminCode") int adminCode);
    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='승인완료' AND a.orderCode = :adminCode ")
    int getAcceptCntByAdminCode(@Param("adminCode")int adminCode);

    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='승인거부' AND a.orderCode = :adminCode ")
    int getDenyCntByAdminCode(@Param("adminCode")int adminCode);

    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='배송중' AND a.orderCode = :adminCode ")

    int getDeliveryCntByAdminCode(@Param("adminCode")int adminCode);
    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='검수대기' AND a.orderCode = :adminCode ")

    int getInspectionWaitCntByAdminCode(@Param("adminCode")int adminCode);
    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition='검수완료' AND a.orderCode = :adminCode ")

    int getInspectionFinishCntByAdminCode(@Param("adminCode")int adminCode);

    @Query("SELECT COUNT(*) FROM Order a WHERE a.orderCondition!='검수완료' AND a.franchise.franchiseOwner.franchiseOwnerCode = :franchiseOwnerCode")
    int findUnprocessedOrder(@Param("franchiseOwnerCode") int franchiseOwnerCode);

    List<Order> findAllByFranchiseDeliveryDriverDriverCode(int driverCode);

    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findAllDesc();
}
