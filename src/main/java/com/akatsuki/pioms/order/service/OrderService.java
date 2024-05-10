package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.order.vo.*;

public interface OrderService {

    OrderListVO getFranchisesOrderList(int adminId);

    boolean postFranchiseOrder(int franchiseCode, RequestOrderVO order);

    OrderListVO getFranchisesUncheckedOrderList(int adminId);

    String acceptOrder(int adminCOde,int orderId);

    String denyOrder(int adminCode,int orderId,String denyMessage);

    OrderListVO getOrderList(int franchiseCode);

    OrderVO getOrder(int franchiseCode,int orderCode);

    OrderVO getAdminOrder(int adminCode, int orderCode);

    boolean putFranchiseOrder(int franchiseCode, RequestPutOrder order);

    boolean putFranchiseOrderCheck(int franchiseCode, RequestPutOrderCheck requestPutOrder);
}