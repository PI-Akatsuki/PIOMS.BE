package com.akatsuki.pioms.order.repository;

import com.akatsuki.pioms.order.aggregate.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository  extends JpaRepository<OrderProduct, Integer> {

    // 발주 코드를 통해 해당 발주의 모든 발주 상품들을 제거합니다.
    void deleteAllByOrderOrderCode(int orderCode);
}
