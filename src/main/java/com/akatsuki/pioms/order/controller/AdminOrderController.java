package com.akatsuki.pioms.order.controller;

import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.service.AdminOrderFacade;
import com.akatsuki.pioms.order.aggregate.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
@Tag(name = "[관리자]발주 API" ,description = "관리자 관련 API 명세서입니다.")
public class AdminOrderController {
    AdminOrderFacade orderFacade;
    @Autowired
    public AdminOrderController(AdminOrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @GetMapping("/order/list")
    @Operation(summary = "관리자가 관리하고 있는 모든 가맹점들의 발주 리스트를 조회합니다.")
    public ResponseEntity<List<OrderVO>> getFranchisesOrderList(
    ){
        List<OrderDTO> orderDTOS = orderFacade.getOrderListByAdminCode();
        if ( orderDTOS==null||orderDTOS.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            return ResponseEntity.ok(null);
        }
        List<OrderVO> orderVOS = new ArrayList<>();
        for (int i = 0; i < orderDTOS.size(); i++) {
            orderVOS.add(new OrderVO(orderDTOS.get(i)));
        }
        return ResponseEntity.ok().body(orderVOS);
    }

    @PutMapping("/order/{orderCode}/accept")
    @Operation(summary = "승인 대기 중인 발주를 승인합니다.")
    public ResponseEntity<Integer> acceptOrder( @PathVariable int orderCode){
        int result = orderFacade.accpetOrder(orderCode);
        if (result==6)
            return ResponseEntity.ok(result);
        if(result==0)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @PutMapping("/order/{orderId}/deny")
    @Operation(summary = "승인 대기 중인 발주를 거절합니다.")
    public ResponseEntity<Integer> denyOrder(@PathVariable int orderId, @RequestParam String denyMessage){
        int result = orderFacade.denyOrder(orderId,denyMessage);
        if (result == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/order/{orderCode}")
    @Operation(summary = "발주를 상세 조회합니다.")
    public ResponseEntity<OrderVO> getOrder(@PathVariable int orderCode){
        OrderDTO orderDTO = orderFacade.getDetailOrderByAdminCode(orderCode);
        if(orderDTO == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(new OrderVO(orderDTO));
    }



}
