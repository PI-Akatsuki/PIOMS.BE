package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.order.aggregate.RequestOrderVO;
import com.akatsuki.pioms.order.aggregate.RequestPutOrder;
import com.akatsuki.pioms.order.aggregate.RequestPutOrderCheck;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.product.service.ProductService;
import com.akatsuki.pioms.specs.service.SpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FranchiseOrderFacade {
    private final OrderService orderService;
    private final InvoiceService invoiceService;
    private final SpecsService specsService;
    private final ExchangeService exchangeService;
    private final ProductService productService;
    private final FranchiseService franchiseService;
    private final FranchiseWarehouseService franchiseWarehouseService;
    private final GetUserInfo getUserInfo;

    @Autowired
    public FranchiseOrderFacade(OrderService orderService, InvoiceService invoiceService, SpecsService specsService, ExchangeService exchangeService, ProductService productService, FranchiseService franchiseService, FranchiseWarehouseService franchiseWarehouseService
        ,GetUserInfo getUserInfo
    ) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.specsService = specsService;
        this.exchangeService = exchangeService;
        this.productService = productService;
        this.franchiseService = franchiseService;
        this.franchiseWarehouseService =franchiseWarehouseService;

        this.getUserInfo = getUserInfo;
    }

    public int postFranchiseOrder(RequestOrderVO requestOrderVO) {
        int franchiseOwnerCode= getUserInfo.getFranchiseOwnerCode();
        System.out.println("franchiseOwnerCode = " + franchiseOwnerCode);
        FranchiseDTO franchise = franchiseService.findFranchiseByFranchiseOwnerCode(franchiseOwnerCode);

        if(!productService.checkOrderEnable(requestOrderVO.getProducts())
                || orderService.findUnprocessedOrder(franchiseOwnerCode)
        )
            return 0;
        int price = productService.getOrderTotalPrice(requestOrderVO.getProducts());
        int result = orderService.postFranchiseOrder(franchise,requestOrderVO,price);
        return result;
    }

    public List<OrderDTO> getOrderListByFranchiseCode() {
        int franchiseOwnerCode= getUserInfo.getFranchiseOwnerCode();
        return orderService.getOrderListByFranchiseOwnerCode(franchiseOwnerCode);
    }


    public boolean putFranchiseOrderCheck(RequestPutOrderCheck requestPutOrder){
        int franchiseOwnerCode= getUserInfo.getFranchiseOwnerCode();
        int franchiseCode = franchiseService.findFranchiseByFranchiseOwnerCode(franchiseOwnerCode).getFranchiseCode();
        OrderDTO order = orderService.getOrder(franchiseCode,requestPutOrder.getOrderCode());
        if (order==null || requestPutOrder.getRequestProduct().size() != order.getOrderProductList().size()){
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
            if(requestPutOrder.getRequestProduct().get(orderProduct.getRequestProductCode())!=null) {
                // changeVal: 받은 수량
                int changeVal = requestPutOrder.getRequestProduct().get(orderProduct.getRequestProductCode());
                // requestVal: 해당 주문상품의 주문수량
                int requestVal = orderProduct.getRequestProductCount();
//                //검수 결과 가맹 창고에 저장
                franchiseWarehouseService.saveProduct(orderProduct.getProductCode(), changeVal, order.getFranchiseCode());
                // 요청 수량과 검수된 수량이 불일치하면 본사 창고에 이를 알려 수정하도록 함
                if(changeVal != requestVal){
                    // 검수 수량 이상 있을 시 본사 창고에 잘못된 수량 업데이트
                    productService.productPlusCnt(orderProduct.getProductCode(), requestVal-changeVal);
                }
            }
        });
        orderService.putOrderCondition(order.getOrderCode(),ORDER_CONDITION.검수완료);

        return true;
    }

    public boolean putFranchiseOrder(RequestPutOrder order) {
        int franchiseOwnerCode= getUserInfo.getFranchiseOwnerCode();
        if(!productService.checkOrderEnable(order.getProducts()) ){
            return false;
        }
        System.out.println("put order processing..");
        int price = productService.getOrderTotalPrice(order.getProducts());
        System.out.println("price = " + price);
        return orderService.putFranchiseOrder(franchiseOwnerCode,order, price);
    }

    public OrderDTO getOrderByFranchiseCode(int orderCode) {
        int franchiseOwnerCode= getUserInfo.getFranchiseOwnerCode();
        int franchiseCode = franchiseService.findFranchiseByFranchiseOwnerCode(franchiseOwnerCode).getFranchiseCode();
        return orderService.getOrder(franchiseCode,orderCode);
    }
}
