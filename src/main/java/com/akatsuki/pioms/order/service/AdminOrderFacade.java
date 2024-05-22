package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.aggregate.RequestPutOrderCheck;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.product.service.ProductService;
import com.akatsuki.pioms.specs.service.SpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminOrderFacade {
    OrderService orderService;
    InvoiceService invoiceService;
    SpecsService specsService;
    ExchangeService exchangeService;
    ProductService productService;
    FranchiseService franchiseService;
    FranchiseWarehouseService franchiseWarehouseService;

    @Autowired
    public AdminOrderFacade(OrderService orderService, InvoiceService invoiceService, SpecsService specsService, ExchangeService exchangeService, ProductService productService, FranchiseService franchiseService, FranchiseWarehouseService franchiseWarehouseService) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.specsService = specsService;
        this.exchangeService = exchangeService;
        this.productService = productService;
        this.franchiseService = franchiseService;
        this.franchiseWarehouseService =franchiseWarehouseService;
    }

    public List<OrderDTO> getOrderListByAdminCode(int adminCode){
        List<Order> orders =  orderService.getOrderListByAdminCode(adminCode);
        List<OrderDTO> orderDTOS = new ArrayList<>();
        orders.forEach(order -> {
            orderDTOS.add(new OrderDTO(order));
        });

        return orderDTOS;
    }
    public List<OrderDTO> getAdminUncheckedOrders(int adminCode){
        return orderService.getAdminUncheckesOrders(adminCode);
    }
    public OrderDTO getAdminOrder(int adminCode, int orderCode){
        return orderService.getAdminOrder(adminCode,orderCode);
    }

    public OrderDTO acceptOrder(int adminCode, int orderCode){
        OrderDTO order = orderService.getAdminOrder(adminCode,orderCode);

        if (order==null || order.getOrderCondition() != ORDER_CONDITION.승인대기 ||!orderService.checkProductCnt(order)){
            // null인지 검사
            // 주문 상태가 승인 대기인지 검사
            // 해당 상품의 수량이 본사 재고를 초과하는지 검사
            System.out.println("error");
            return null;
        }

        ExchangeDTO exchange =  exchangeService.findExchangeToSend(order.getFranchiseCode());

        if(exchange!=null) {
            // 교환 가능 여부 검사
            if(productService.checkExchangeProduct(order,exchange) ){
                order = orderService.addExchangeToOrder(exchange, order.getOrderCode());
                productService.exportExchangeProducts(exchange.getExchangeCode());
            }
        }
        productService.exportProducts(order);

        order = orderService.acceptOrder(adminCode,orderCode, exchange);
        System.out.println("order = " + order);
        specsService.afterAcceptOrder(orderCode, order.getFranchiseCode(), order.getDeliveryDate());
        invoiceService.afterAcceptOrder(order);

        return order;
    }

    public OrderDTO denyOrder(int adminCode,int orderId, String denyMessage){
        return orderService.denyOrder(adminCode,orderId,denyMessage);

    }

}
