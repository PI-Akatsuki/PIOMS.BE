package com.akatsuki.pioms.order.controller;

import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.service.OrderFacade;
import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.order.aggregate.OrderListVO;
import com.akatsuki.pioms.order.aggregate.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@Tag(name = "Admin Order API" ,description = "관리자 관련 API 명세서입니다.")
public class AdminOrderController {
    OrderFacade orderFacade;

    @Autowired
    public AdminOrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @GetMapping("/{adminCode}/orders")
    @Operation(summary = "관리자가 관리하고 있는 모든 가맹점들의 발주 리스트를 조회합니다.")
    public ResponseEntity<List<OrderDTO>> getFranchisesOrderList(@PathVariable int adminCode){

        List<OrderDTO> orderListVO = orderFacade.getOrderListByAdminCode(adminCode);

        return ResponseEntity.ok().body(orderListVO);
    }
    /**
     * <h2>모든 가맹점 승인대기 발주 목록 조회</h2>
     * */
    @GetMapping("/{adminCode}/unchecked-orders")
    @Operation(summary = "관리자가 관리하는 모든 가맹점들 중 승인 하지 않은 발주 리스틀 조회합니다.")
    public ResponseEntity<OrderListVO> getFranchisesUncheckedOrderList(@PathVariable int adminCode){
        OrderListVO orderListVO = new OrderListVO(orderFacade.getAdminUncheckesOrders(adminCode));
        return ResponseEntity.ok().body(orderListVO);
    }

    @PutMapping("/{adminCode}/order/{orderCode}/accept")
    @Operation(summary = "승인 대기 중인 발주를 승인합니다.")
    public ResponseEntity<OrderVO> acceptOrder(@PathVariable int adminCode, @PathVariable int orderCode){
        OrderDTO returnValue = orderFacade.acceptOrder(adminCode, orderCode);
        return ResponseEntity.ok(new OrderVO(returnValue));
    }
    @PutMapping("/{adminCode}/order/{orderId}/deny")
    @Operation(summary = "승인 대기 중인 발주를 거절합니다.")
    public ResponseEntity<String> denyOrder(@PathVariable int adminCode,@PathVariable int orderId, @RequestParam String denyMessage){
        String returnValue = orderFacade.denyOrder(adminCode,orderId,denyMessage);
        return ResponseEntity.ok(returnValue);
    }

    @GetMapping("/{adminCode}/order/{orderCode}")
    @Operation(summary = "발주를 상세 조회합니다.")
    public ResponseEntity<OrderVO> getOrder(@PathVariable int adminCode, @PathVariable int orderCode){
        OrderVO order = new OrderVO(orderFacade.getAdminOrder(adminCode,orderCode));
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
        }
        return ResponseEntity.ok(order);
    }
}
