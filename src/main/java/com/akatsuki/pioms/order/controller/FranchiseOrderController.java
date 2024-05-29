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
    public ResponseEntity<OrderDTO> postFranchiseOrder(@RequestParam int franchiseOwnerCode, @RequestBody RequestOrderVO orders){
        OrderDTO result = franchiseOrderFacade.postFranchiseOrder(franchiseOwnerCode,orders);
        System.out.println("result = " + result);
        if(result == null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        return ResponseEntity.ok().body(result);
    }
    @PutMapping("/order")
    public ResponseEntity<String> putFranchiseOrder(@RequestParam int franchiseOwnerCode, @RequestBody RequestPutOrder order){
        boolean result = franchiseOrderFacade.putFranchiseOrder(franchiseOwnerCode, order);
        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("put failed. check again");
        return ResponseEntity.ok("put finished");
    }
    @PutMapping("/order/check")
    public ResponseEntity<String> putFranchiseOrderCheck(@RequestParam int franchiseOwnerCode, @RequestBody RequestPutOrderCheck requestPutOrder){
        System.out.println("requestPutOrder = " + requestPutOrder);
        boolean result = franchiseOrderFacade.putFranchiseOrderCheck(franchiseOwnerCode,requestPutOrder);
        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("put failed");
        return ResponseEntity.ok("put finished");
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderListVO> getFranchiseOrderList(@RequestParam int franchiseOwnerCode){
        List<OrderDTO> orders = franchiseOrderFacade.getOrderListByFranchiseCode(franchiseOwnerCode);
        if (orders.isEmpty() ){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok(new OrderListVO(orders));
    }

    @GetMapping("/order/{orderCode}")
    public ResponseEntity<OrderVO> getOrder(@RequestParam int franchiseOwnerCode, @PathVariable int orderCode){
        OrderDTO orderDTO = franchiseOrderFacade.getOrderByFranchiseCode(franchiseOwnerCode,orderCode);
        if(orderDTO==null)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        OrderVO orderVO = new OrderVO(orderDTO);
        return ResponseEntity.ok(orderVO);
    }
}
