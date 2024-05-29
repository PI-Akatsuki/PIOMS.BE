package com.akatsuki.pioms.order.controller;

import com.akatsuki.pioms.order.aggregate.*;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.service.AdminOrderFacade;
import com.akatsuki.pioms.order.service.FranchiseOrderFacade;
import com.akatsuki.pioms.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/franchise")
public class FranchiseOrderController {
    FranchiseOrderFacade franchiseOrderFacade;

    @Autowired
    public FranchiseOrderController( FranchiseOrderFacade franchiseOrderFacade) {
        this.franchiseOrderFacade = franchiseOrderFacade;
    }

    /**
     * <h2>발주 생성</h2>
     * */
    @PostMapping("/order")
    public ResponseEntity<Integer> postFranchiseOrder(@RequestBody RequestOrderVO orders){
        int result = franchiseOrderFacade.postFranchiseOrder(orders);

        if(result== -1)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR ).build();
        if(result == 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/order")
    public ResponseEntity<String> putFranchiseOrder( @RequestBody RequestPutOrder order){
        boolean result = franchiseOrderFacade.putFranchiseOrder(order);
        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("put failed. check again");
        return ResponseEntity.ok("put finished");
    }

    @PutMapping("/order/check")
    public ResponseEntity<String> putFranchiseOrderCheck(@RequestBody RequestPutOrderCheck requestPutOrder){
        System.out.println("requestPutOrder = " + requestPutOrder);
        boolean result = franchiseOrderFacade.putFranchiseOrderCheck(requestPutOrder);
        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("put failed");
        return ResponseEntity.ok("put finished");
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderListVO> getFranchiseOrderList(){
        List<OrderDTO> orders = franchiseOrderFacade.getOrderListByFranchiseCode();
        if (orders.isEmpty() ){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(new OrderListVO(orders));
    }

    @GetMapping("/order/{orderCode}")
    public ResponseEntity<OrderVO> getOrder(@PathVariable int orderCode){
        OrderDTO orderDTO = franchiseOrderFacade.getOrderByFranchiseCode(orderCode);
        if(orderDTO==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        OrderVO orderVO = new OrderVO(orderDTO);
        return ResponseEntity.ok(orderVO);
    }
}
