package com.akatsuki.pioms.order.controller;

import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.order.vo.OrderListVO;
import com.akatsuki.pioms.order.vo.OrderVO;
import com.akatsuki.pioms.order.vo.RequestOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/franchise")
public class FranchiseOrderController {
    OrderService orderService;

    @Autowired
    public FranchiseOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * <h2>발주 생성</h2>
     * */
    @PostMapping("/franchise/{franchiseCode}")
    public ResponseEntity<String> postFranchiseOrder(@PathVariable int franchiseCode, @RequestBody RequestOrderVO orders){
        boolean result = orderService.postFranchiseOrder(franchiseCode,orders);
        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        return ResponseEntity.ok().body("Post finished");
    }

    @GetMapping("/franchise/{franchiseCode}")
    public ResponseEntity<OrderListVO> getFranchiseOrderList(@PathVariable int franchiseCode){
        return ResponseEntity.ok(orderService.getOrderList(franchiseCode));
    }

    @GetMapping("/franchise/{franchiseCode}/order/{orderCode}")
    public ResponseEntity<OrderVO> getOrder(@PathVariable int franchiseCode, @PathVariable int orderCode){
        OrderVO orderVO = orderService.getOrder(franchiseCode,orderCode);
        if(orderVO==null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        return ResponseEntity.ok(orderVO);
    }
}
