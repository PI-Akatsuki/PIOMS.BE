package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.exchange.aggregate.RequestExchange;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.aggregate.RequestPutOrderCheck;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.product.service.ProductService;
import com.akatsuki.pioms.specs.service.SpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FranchiseOrderFacade {
    OrderService orderService;
    InvoiceService invoiceService;
    SpecsService specsService;
    ExchangeService exchangeService;
    ProductService productService;
    FranchiseService franchiseService;
    FranchiseWarehouseService franchiseWarehouseService;

    @Autowired
    public FranchiseOrderFacade(OrderService orderService, InvoiceService invoiceService, SpecsService specsService, ExchangeService exchangeService, ProductService productService, FranchiseService franchiseService, FranchiseWarehouseService franchiseWarehouseService) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.specsService = specsService;
        this.exchangeService = exchangeService;
        this.productService = productService;
        this.franchiseService = franchiseService;
        this.franchiseWarehouseService =franchiseWarehouseService;
    }

    public OrderDTO postFranchiseOrder(int franchiseCode, RequestOrderVO orders) {
        Franchise franchise = franchiseService.findFranchiseById(franchiseCode).orElseThrow();
        if(!productService.checkPostOrderEnable(orders.getProducts()))
            return null;

        return orderService.postFranchiseOrder(franchise,orders);
    }

    public List<OrderDTO> getOrderListByFranchiseCode(int franchiseCode) {
        return orderService.getOrderList(franchiseCode);
    }

    public boolean putFranchiseOrderCheck(int franchiseCode, RequestPutOrderCheck requestPutOrder){
        OrderDTO order = orderService.getOrder(franchiseCode,requestPutOrder.getOrderCode());
        if (order==null){
            return false;
        }
        // 검증 1. 요청한 가맹 코드와 발송 코드 일치 여부
        // 검증 2. 주문 상태가 검수 대기인지
        // 검증 3. 해당 주문에 대한 배송 상태가 배송완료인지
        if(franchiseCode != order.getFranchiseCode() || order.getOrderCondition() != ORDER_CONDITION.검수대기
                || !invoiceService.checkInvoiceStatus(order.getOrderCode())
        ){
            return false;
        }

        order.getOrderProductList().forEach(orderProduct->{
            if(requestPutOrder.getRequestProduct().get(orderProduct.getProductCode())!=null) {
                // changeVal: 받은 수량
                int changeVal = requestPutOrder.getRequestProduct().get(orderProduct.getProductCode());
                // requestVal: 해당 주문상품의 주문수량
                int requestVal = orderProduct.getRequestProductCount();
//                //검수 결과 가맹 창고에 저장
                franchiseWarehouseService.saveProduct(orderProduct.getProductCode(), changeVal, order.getFranchiseCode());
                // 요청 수량과 검수된 수량이 불일치하면 본사 창고에 이를 알려 수정하도록 함
                if(changeVal != requestVal){
                    // 검수 수량 이상 있을 시 본사 창고에 잘못된 수량 업데이트
                    productService.editIncorrectCount(orderProduct.getProductCode(), requestVal-changeVal);
                }
            }
        });
        orderService.putOrderCondition(order.getOrderCode(),ORDER_CONDITION.검수완료);
//        // 가맹 창고 업데이트
//        franchiseWarehouseService.saveExchangeProduct(order.getExchange(), franchiseCode);

        return true;
    }
}
