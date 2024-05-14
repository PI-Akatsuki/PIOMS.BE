package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.exchange.aggregate.Exchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.service.ProductService;
import com.akatsuki.pioms.specs.service.SpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

    ApplicationEventPublisher publisher;

    @Autowired
    public OrderFacade(OrderService orderService, InvoiceService invoiceService, SpecsService specsService, ExchangeService exchangeService, ProductService productService, FranchiseService franchiseService, ApplicationEventPublisher publisher) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.specsService = specsService;
        this.exchangeService = exchangeService;
        this.productService = productService;
        this.franchiseService = franchiseService;
        this.publisher = publisher;
    }

    public List<OrderDTO> getOrderListByAdminCode(int adminCode){
        List<Order> orders =  orderService.getOrderListByAdminCode(adminCode);
        List<OrderDTO> orderDTOS = new ArrayList<>();
        orders.forEach(order -> {
            orderDTOS.add(new OrderDTO(order));
        });
        return orderDTOS;
    }

    public List<OrderDTO> getAdminUncheckesOrders(int adminCode){
        List<Order> orders = orderService.getAdminUncheckesOrders(adminCode);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            orderDTOs.add(new OrderDTO(orders.get(i)));
        }
        return orderDTOs;
    }
    public OrderDTO getAdminOrder(int adminCode, int orderCode){
        return orderService.getAdminOrder(adminCode,orderCode);
    }

    public Order acceptOrder(int adminCode, int orderCode){
        OrderDTO order = orderService.getAdminOrder(adminCode,orderCode);
        ExchangeDTO exchange =  exchangeService.findExchangeToSend(order.getFranchiseCode());

        if(!orderService.checkProductCnt(order)) {
            return null;
        }

        if(!productService.checkExchangeProduct(order,exchange)) {
            exchange = null;
        }

        Order orderDTO = orderService.acceptOrder(adminCode,orderCode, exchange);
        productService.exportProducts(order);
        if (exchange!=null)
            productService.exportExchangeProducts(exchange.getExchangeCode());
//        publisher.publishEvent(new OrderEvent(order));

        return orderDTO;
    }

    public String denyOrder(int adminCode,int orderId, String denyMessage){
        String returnValue = orderService.denyOrder(adminCode,orderId,denyMessage);
        return returnValue;
    }

}
