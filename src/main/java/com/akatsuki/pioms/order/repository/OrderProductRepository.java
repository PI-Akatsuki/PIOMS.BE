package com.akatsuki.pioms.order.repository;

import com.akatsuki.pioms.order.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository  extends JpaRepository<OrderProduct, Integer> {

    void deleteAllByOrderOrderCode(int orderCode);
}
