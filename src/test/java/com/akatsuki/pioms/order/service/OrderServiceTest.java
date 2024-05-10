package com.akatsuki.pioms.order.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @BeforeEach
    void setUp() {
        
    }

    @Test
    @DisplayName("관리자의 모든 가맹점 리스트 조회")
    void getFranchisesOrderList() {

    }

    @Test
    void postFranchiseOrder() {
    }

    @Test
    void getFranchisesUncheckedOrderList() {
    }

    @Test
    void acceptOrder() {
    }

    @Test
    void denyOrder() {
    }

    @Test
    void getOrderList() {
    }

    @Test
    void getOrder() {
    }

    @Test
    void getAdminOrder() {
    }

    @Test
    void putFranchiseOrder() {
    }

    @Test
    void putFranchiseOrderCheck() {
    }
}