package com.akatsuki.pioms.order.controller;

import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.order.vo.OrderListVO;
import com.akatsuki.pioms.order.vo.OrderVO;
import com.akatsuki.pioms.order.vo.RequestOrderVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>발주 API</h1>
 * <br>
 * <h2>공통</h2>
 * 1. 상세 조회(/order/{orderId})<br>
 * 2.

 * <br><h2>관리자</h2>
 * 1. 모든 가맹점 발주 목록 조회(order/admin/{adminCode}/orders)<br>
 * 2. 모든 가맹점 미처리된 발주 조회(order/admin/{adminCode}/orders/unchecked)<br>
 * 3. 발주 승인(order/{orderId}/accept)<br>
 * 4. 발주 반려(order/{orderId}/deny)<br>

 * <br><h2>점주</h2>
 * 1. 발주 목록 조회(order/franchise/{franchiseId}/orders)<br>
 * 2. 신청 대기중인 발주 조회(order/franchise/orders/unchecked)<br>
 * 3. 거부 된 발주 조회(order/franchise/orders/denied)<br>
 * 4. 발주 신청하기(order/franchise)
 * */


@RestController
@RequestMapping("/admin")
public class AdminOrderController {
    OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * <h2>모든 가맹점 발주 목록 조회</h2>
     * */
    @GetMapping("/{adminCode}/orders")
    public ResponseEntity<OrderListVO> getFranchisesOrderList(@PathVariable int adminCode){
        OrderListVO orderListVO = orderService.getFranchisesOrderList(adminCode);
        return ResponseEntity.ok().body(orderListVO);
    }
    /**
     * <h2>모든 가맹점 승인대기 발주 목록 조회</h2>
     * */
    @GetMapping("/{adminCode}/unchecked-orders")
    public ResponseEntity<OrderListVO> getFranchisesUncheckedOrderList(@PathVariable int adminCode){
        OrderListVO orderListVO = orderService.getFranchisesUncheckedOrderList(adminCode);
        return ResponseEntity.ok().body(orderListVO);
    }

    @PutMapping("/{adminCode}/order/{orderId}/accept")
    public ResponseEntity<String> acceptOrder(@PathVariable int adminCode,@PathVariable int orderId){
        String returnValue = orderService.acceptOrder(adminCode, orderId);
        return ResponseEntity.ok(returnValue);
    }
    @PutMapping("/{adminCode}/order/{orderId}/deny")
    public ResponseEntity<String> denyOrder(@PathVariable int adminCode,@PathVariable int orderId, @RequestParam String denyMessage){
        String returnValue = orderService.denyOrder(adminCode,orderId,denyMessage);
        return ResponseEntity.ok(returnValue);
    }

    @GetMapping("/{adminCode}/order/{orderCode}")
    public ResponseEntity<OrderVO> getOrder(@PathVariable int adminCode, @PathVariable int orderCode){
        OrderVO order = orderService.getAdminOrder(adminCode,orderCode);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        return ResponseEntity.ok(order);
    }
}
