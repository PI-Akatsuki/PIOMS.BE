package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.dto.OrderProductDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.product.service.ProductService;
import com.akatsuki.pioms.specs.service.SpecsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminOrderFacade {
    private final OrderService orderService;
    private final InvoiceService invoiceService;
    private final SpecsService specsService;
    private final ExchangeService exchangeService;
    private final ProductService productService;
    private final GetUserInfo getUserInfo;

    public List<OrderDTO> getOrderListByAdminCode(){
        int adminCode= getUserInfo.getAdminCode();
        List<OrderDTO> orders =  orderService.getOrderListByAdminCode(adminCode);
        return orders;
    }

    public OrderDTO getDetailOrderByAdminCode(int orderCode){
        int adminCode= getUserInfo.getAdminCode();
        return orderService.getDetailOrderByAdminCode(adminCode,orderCode);
    }

    /**
     <h1>accept Order</h1>
     <h2>return value</h2>
     0: This order's condition is not '승인대기' || find exchange to send franchise fail <br>
     1: Fail Product logic<br>
     2: Fail change order's conditions or put exchange to order<br>
     3: Fail Specs logic<br>
     4: Fail Invoice logic<br>
     5: Fail Exchange logic<br>
     6: Success!! */
    @Transactional(readOnly = false)
    public int accpetOrder(int orderCode){

        int adminCode= getUserInfo.getAdminCode();
        int success=0;
        try {

            OrderDTO order = orderService.getOrderById(orderCode);
            if(order.getAdminCode()!=adminCode &&adminCode!=1){
                throw new Exception("접근 권한 없음");
            }
            if (order.getOrderCondition() != ORDER_CONDITION.승인대기 ||
                    !productService.checkOrderEnable(convertListToMap(order.getOrderProductList()) ))
                throw new Exception("승인 대기가 아님");
            ExchangeDTO exchangeDTO = findExchangeToAdd(order);// 회사로 보낼 반품/교환 탐색
            success++; // 1

            // change order condition '승인완료', add Exchange in order
            order = orderService.acceptOrder(adminCode,orderCode,exchangeDTO);
            if(order == null)
                throw new Exception("accpet Order problem occured!!");
            success++; // 2

            success +=afterAcceptOrder(order);

            return success;
        }catch (Exception e){
            return success;
        }
    }

    private ExchangeDTO findExchangeToAdd(OrderDTO order) {

        ExchangeDTO exchangeDTO = exchangeService.findExchangeToSend(order.getFranchiseCode());
        // 반품 요소 있는지 검사
        if (exchangeDTO!=null && !productService.checkExchangeProduct(order,exchangeDTO)){
            exchangeDTO = null;
        }
        return exchangeDTO;
    }

    @Transactional
    public int afterAcceptOrder(OrderDTO order){
        int success=0;
        productService.exportProducts(order);  // 본사 재고 수정
        success++; // 3
        specsService.afterAcceptOrder(order); // 명세서 생성
        success++; // 4
        invoiceService.afterAcceptOrder(order); // 배송 생성
        success++; // 5
        exchangeService.afterAcceptOrder(order); // 본사에서 가맹으로 돌려줄 교환 제품 탐색
        success++; // 6
        return success;
    }


    private Map<Integer, Integer> convertListToMap(List<OrderProductDTO> orderProductList) {

        Map<Integer,Integer> result = new HashMap<>();
        for (int i = 0; i < orderProductList.size(); i++) {
            OrderProductDTO orderProductDTO = orderProductList.get(i);
            result.put(orderProductDTO.getProductCode(),orderProductDTO.getRequestProductCount());
        }
        return result;
    }

    public int denyOrder(int orderId, String denyMessage){

        int adminCode= getUserInfo.getAdminCode();
        return orderService.denyOrder(adminCode,orderId,denyMessage);
    }

}
