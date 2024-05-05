package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.order.vo.OrderListVO;
import com.akatsuki.pioms.order.vo.OrderVO;

public interface OrderService {

    OrderListVO getFranchisesOrderList(int adminId);

    void postFranchiseOrder(OrderVO order);

    OrderListVO getFranchisesUncheckedOrderList(int adminId);
}
