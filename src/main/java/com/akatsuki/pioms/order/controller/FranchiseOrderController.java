package com.akatsuki.pioms.order.controller;

import com.akatsuki.pioms.order.aggregate.*;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.service.OrderFacade;
import com.akatsuki.pioms.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/franchise")
public class FranchiseOrderController {
    OrderService orderService;
    OrderFacade orderFacade;

    @Autowired
    public FranchiseOrderController(OrderService orderService,OrderFacade orderFacade) {
        this.orderService = orderService;
        this.orderFacade = orderFacade;
    }

    /**
     * <h2>발주 생성</h2>
     * */
    @PostMapping("/{franchiseCode}")
    public ResponseEntity<OrderDTO> postFranchiseOrder(@PathVariable int franchiseCode, @RequestBody RequestOrderVO orders){
        OrderDTO result = orderFacade.postFranchiseOrder(franchiseCode,orders);
        if(result == null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        return ResponseEntity.ok().body(result);
    }
    @PutMapping("/{franchiseCode}")
    public ResponseEntity<String> putFranchiseOrder(@PathVariable int franchiseCode, @RequestBody RequestPutOrder order){
        boolean result = orderService.putFranchiseOrder(franchiseCode, order);
        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("put failed. check again");
        return ResponseEntity.ok("put finished");
    }
    @PutMapping("/{franchiseCode}/check")
    public ResponseEntity<String> putFranchiseOrder(@PathVariable int franchiseCode, @RequestBody RequestPutOrderCheck requestPutOrder){
        boolean result = orderFacade.putFranchiseOrderCheck(franchiseCode,requestPutOrder);
        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("put failed");
        return ResponseEntity.ok("put finished");
    }

    @GetMapping("/{franchiseCode}/orders")
    public ResponseEntity<OrderListVO> getFranchiseOrderList(@PathVariable int franchiseCode){
        return ResponseEntity.ok(new OrderListVO(orderFacade.getOrderListByFranchiseCode(franchiseCode)));
    }

    @GetMapping("/{franchiseCode}/order/{orderCode}")
    public ResponseEntity<OrderVO> getOrder(@PathVariable int franchiseCode, @PathVariable int orderCode){
        OrderVO orderVO = new OrderVO(orderService.getOrder(franchiseCode,orderCode));
        if(orderVO==null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        return ResponseEntity.ok(orderVO);
    }
}
