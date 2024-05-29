package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.product.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ExchangeFacade {
    private final FranchiseWarehouseService franchiseWarehouseService;
    private final OrderService orderService ;
    private final FranchiseService franchiseService;
    private final ProductService productService;
    private final ExchangeService exchangeService;

    public ExchangeFacade(FranchiseWarehouseService franchiseWarehouseService, OrderService orderService, FranchiseService franchiseService, ProductService productService, ExchangeService exchangeService) {
        this.franchiseWarehouseService = franchiseWarehouseService;
        this.orderService = orderService;
        this.franchiseService = franchiseService;
        this.productService = productService;
        this.exchangeService = exchangeService;
    }

}
