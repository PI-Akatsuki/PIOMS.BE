package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.exchange.aggregate.Exchange;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.service.ProductService;
import com.akatsuki.pioms.specs.service.SpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

    public List<Order> getOrderListByAdminCode(int adminCode){
        List<Order> orders =  orderService.getOrderListByAdminCode(adminCode);
        return orders;
    }

    public List<Order> getAdminUncheckesOrders(int adminCode){
        List<Order> orders = orderService.getAdminUncheckesOrders(adminCode);
        return orders;
    }
    public Order getAdminOrder(int adminCode, int orderCode){
        return orderService.getAdminOrder(adminCode,orderCode);
    }

    public Order acceptOrder(int adminCode, int orderCode){
        Order order = orderService.getAdminOrder(adminCode,orderCode);
        ExchangeDTO exchange =  exchangeService.findExchangeToSend(order.getFranchise().getFranchiseCode());

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

        publisher.publishEvent(new OrderEvent(order));

        return orderDTO;
    }

    public String denyOrder(int adminCode,int orderId, String denyMessage){
        String returnValue = orderService.denyOrder(adminCode,orderId,denyMessage);
        return returnValue;
    }

}
