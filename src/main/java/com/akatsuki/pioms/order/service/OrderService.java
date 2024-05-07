package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.order.vo.OrderListVO;
import com.akatsuki.pioms.order.vo.OrderVO;
import com.akatsuki.pioms.order.vo.RequestOrderVO;

public interface OrderService {

    OrderListVO getFranchisesOrderList(int adminId);

    void postFranchiseOrder(RequestOrderVO order);

    OrderListVO getFranchisesUncheckedOrderList(int adminId);

    String acceptOrder(int orderId);

    String denyOrder(int orderId,String denyMessage);
}
