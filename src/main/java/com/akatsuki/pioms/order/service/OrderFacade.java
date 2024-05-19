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
public class OrderFacade {
    OrderService orderService;
    InvoiceService invoiceService;
    SpecsService specsService;
    ExchangeService exchangeService;
    ProductService productService;
    FranchiseService franchiseService;
    FranchiseWarehouseService franchiseWarehouseService;

    ApplicationEventPublisher publisher;

    @Autowired
    public OrderFacade(OrderService orderService, InvoiceService invoiceService, SpecsService specsService, ExchangeService exchangeService, ProductService productService, FranchiseService franchiseService, FranchiseWarehouseService franchiseWarehouseService) {
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
            return null;
        }

        ExchangeDTO exchange =  exchangeService.findExchangeToSend(order.getFranchiseCode());
        System.out.println("exchange = " + exchange);
        if(exchange!=null) {
            // 교환 가능 여부 검사
            if(productService.checkExchangeProduct(order,exchange) ){
                order = orderService.addExchangeToOrder(exchange, order.getOrderCode());
                productService.exportExchangeProducts(exchange.getExchangeCode());
            }
        }
        productService.exportProducts(order);
        Order orderEntity = orderService.acceptOrder(adminCode,orderCode, exchange);

        specsService.afterAcceptOrder(orderCode, order.getFranchiseCode(), order.getDeliveryDate());
        invoiceService.afterAcceptOrder(order);

        return new OrderDTO(orderEntity);
    }

    public OrderDTO denyOrder(int adminCode,int orderId, String denyMessage){
        return orderService.denyOrder(adminCode,orderId,denyMessage);

    }

    public OrderDTO postFranchiseOrder(int franchiseCode, RequestOrderVO orders) {
        Franchise franchise = franchiseService.findFranchiseById(franchiseCode).orElseThrow();
        return orderService.postFranchiseOrder(franchise,orders);
    }

    public List<OrderDTO> getOrderListByFranchiseCode(int franchiseCode) {
        return orderService.getOrderList(franchiseCode);
    }

    public boolean putFranchiseOrderCheck(int franchiseCode, RequestPutOrderCheck requestPutOrder){
        System.out.println("requestPutOrder = " + requestPutOrder);
        OrderDTO order = orderService.getOrder(franchiseCode,requestPutOrder.getOrderCode());
        System.out.println("order.getOrderProductList() = " + order.getOrderProductList());
        if (order==null){
            return false;
        }
        if(franchiseCode != order.getFranchiseCode() || order.isOrderStatus()
                || !invoiceService.checkInvoiceStatus(order.getOrderCode())){
            return false;
        }

        order.getOrderProductList().forEach(orderProduct->{
            if(requestPutOrder.getRequestProduct().get(orderProduct.getProductCode())!=null) {
                int changeVal = requestPutOrder.getRequestProduct().get(orderProduct.getProductCode());
                int requestVal = orderProduct.getRequestProductCount();
//                //검수 결과 가맹 창고에 저장
                franchiseWarehouseService.saveProduct(orderProduct.getProductCode(), changeVal, order.getFranchiseCode());
                if(changeVal != requestVal){
                    // 검수 수량 이상 있을 시 본사 창고에 잘못된 수량 업데이트~
                    productService.editIncorrectCount(orderProduct.getProductCode(), requestVal-changeVal);
                }
            }
        });
//        // 가맹 창고 업데이트
        franchiseWarehouseService.saveExchangeProduct(order.getExchange(), franchiseCode);
        return true;
    }
}
