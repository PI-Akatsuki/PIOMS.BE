package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.order.vo.OrderListVO;

public interface OrderService {

    OrderListVO getFranchisesOrderList(int adminId);
}
