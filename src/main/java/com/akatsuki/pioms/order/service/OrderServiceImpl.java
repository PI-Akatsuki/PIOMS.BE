package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.entity.ExchangeEntity;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.order.entity.OrderEntity;
import com.akatsuki.pioms.order.entity.OrderProductEntity;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.repository.OrderProductRepository;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.vo.*;
import com.akatsuki.pioms.product.entity.Product;
import com.akatsuki.pioms.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    OrderRepository orderRepository;
    ApplicationEventPublisher publisher;
    ExchangeService exchangeService;
    OrderProductRepository orderProductRepository;
    ProductService productService;
    InvoiceService invoiceService;
    FranchiseWarehouseService franchiseWarehouseService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ApplicationEventPublisher publisher, ExchangeService exchangeService, OrderProductRepository orderProductRepository,ProductService productService, InvoiceService invoiceService, FranchiseWarehouseService franchiseWarehouseService) {
        this.orderRepository = orderRepository;
        this.publisher = publisher;
        this.exchangeService = exchangeService;
        this.orderProductRepository = orderProductRepository;
        this.productService = productService;
        this.invoiceService = invoiceService;
        this.franchiseWarehouseService = franchiseWarehouseService;
    }


    @Override
    @Transactional(readOnly = true)
    public OrderListVO getFranchisesOrderList(int adminCode){
        List<OrderEntity> orderList = orderRepository.findAllByFranchiseAdminAdminCode(adminCode);
        System.out.println("orderList = " + orderList);
        List<OrderVO> orderVOList = new ArrayList<>();
        orderList.forEach(order-> {
            orderVOList.add(new OrderVO(order));
        });
        return new OrderListVO(orderVOList);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListVO getFranchisesUncheckedOrderList(int adminCode){
        List<OrderEntity> orderList = orderRepository.findAllByFranchiseAdminAdminCodeAndOrderCondition(adminCode, ORDER_CONDITION.승인대기);
        System.out.println("orderList = " + orderList);
        List<OrderVO> orderVOList = new ArrayList<>();
        orderList.forEach(order-> {
            orderVOList.add(new OrderVO(order));
        });
        return new OrderListVO(orderVOList);
    }

    @Override
    @Transactional(readOnly = false)
    public String acceptOrder(int adminCode,int orderId) {

        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        if(order.getFranchise().getAdmin().getAdminCode() != adminCode){
            return "You dont\'t have permission to manage this franchise";
        }
        if (!checkOrderCondition(order))
            return "This order is unavailable to accept. This order's condition is '" + order.getOrderCondition().name() + "', not '승인대기'. ";

        try {
            order.setOrderCondition(ORDER_CONDITION.승인완료);

            findExchange(order);
            orderRepository.save(order);
            publisher.publishEvent(new OrderEvent(order));
        }catch (Exception e){
            System.out.println("exception occuered");
        }

        return "This order is accepted";
    }



    @Override
    @Transactional
    public String denyOrder(int adminCode,int orderId, String denyMessage){
        OrderEntity order = orderRepository.findById(orderId).orElseThrow();
        if(order.getFranchise().getAdmin().getAdminCode() != adminCode){
            return "You dont\'t have permission to manage this franchise";
        }
        if (!checkOrderCondition(order))
            return "This order is unavailable to accept. This order's condition is '" + order.getOrderCondition().name() + "', not '승인대기'. ";;
        order.setOrderCondition(ORDER_CONDITION.승인거부);
        order.setOrderReason(denyMessage);
        orderRepository.save(order);
        return "This order is denied.";
    }

    @Override
    @Transactional
    public boolean postFranchiseOrder(int franchiseCode,RequestOrderVO requestOrder){
        if(franchiseCode != requestOrder.getFranchiseCode()){
            System.out.println("가맹점 코드, 주문의 가맹점 코드 불일치! ");
            return false;
        }
        OrderEntity order = new OrderEntity();

        order.setOrderDate(LocalDateTime.now());
        order.setOrderCondition(ORDER_CONDITION.승인대기);
        order.setOrderStatus(false);
        Franchise franchise = new Franchise();
        franchise.setFranchiseCode(requestOrder.getFranchiseCode());
        order.setFranchise(franchise);
        order= orderRepository.save(order);

        int orderId = order.getOrderCode();
        requestOrder.getProducts().forEach((productId, count)->{
            OrderEntity order1 = orderRepository.findById(orderId).orElseThrow();
            Product product = productService.getProduct(productId);
            orderProductRepository.save(new OrderProductEntity(count,0, order1, product));
        });
        return true;
    }

    private static boolean checkOrderCondition(OrderEntity order) {
        if(order.getOrderCondition() != ORDER_CONDITION.승인대기){
            return false;
        }
        return true;
    }

    private void findExchange(OrderEntity order) {
        // 반품신청 중인 요소 탐색
        ExchangeDTO exchange =  exchangeService.findExchangeToSend(order.getFranchise().getFranchiseCode());

        if(exchange!=null) {
            order.setExchange(new ExchangeEntity(exchange));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderListVO getOrderList(int franchiseCode){
        List<OrderEntity> orderList= orderRepository.findByFranchiseFranchiseCode(franchiseCode);
        List<OrderVO> orderVOList = new ArrayList<>();
        orderList.forEach(order-> {
            orderVOList.add(new OrderVO(order));
        });
        return new OrderListVO(orderVOList);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderVO getOrder(int franchiseCode,int orderCode){
        OrderEntity order = orderRepository.findById(orderCode).orElseThrow();
        if(franchiseCode!= order.getFranchise().getFranchiseCode()){
            System.out.println("가맹점 코드, 주문의 가맹점 코드 불일치!");
            return null;
        }
        System.out.println("order = " + order);
        System.out.println(order.getOrderProductList());
        return new OrderVO(order);
    }

    @Override
    public OrderVO getAdminOrder(int adminCode, int orderCode) {
        OrderEntity order = orderRepository.findById(orderCode).orElseThrow(IllegalArgumentException::new);
        if(adminCode != order.getFranchise().getAdmin().getAdminCode()){
            return null;
        }
        return new OrderVO(order);
    }

    @Override
    @Transactional
    public boolean putFranchiseOrder(int franchiseCode, RequestPutOrder requestOrder) {
        OrderEntity order = orderRepository.findById(requestOrder.getOrderCode()).orElseThrow(IllegalArgumentException::new);
        if(order.getFranchise().getFranchiseCode() != franchiseCode)
            return false;
        orderProductRepository.deleteAllByOrderOrderCode(order.getOrderCode());
        OrderEntity deletedorder = orderRepository.findById(requestOrder.getOrderCode()).orElseThrow(IllegalArgumentException::new);

        requestOrder.getProducts().forEach((productId, count)->{
            Product product = productService.getProduct(productId);
            orderProductRepository.save(new OrderProductEntity(count,0, deletedorder, product));
        });
        System.out.println(deletedorder.getOrderProductList());
        orderRepository.save(deletedorder);
        return true;
    }

    @Override
    public boolean putFranchiseOrderCheck(int franchiseCode, RequestPutOrderCheck requestPutOrder) {
        OrderEntity order = orderRepository.findById(requestPutOrder.getOrderCode()).orElseThrow(IllegalArgumentException::new);
        System.out.println("requestPutOrder = " + requestPutOrder);
        if(franchiseCode != order.getFranchise().getFranchiseCode() || order.isOrderStatus() || !invoiceService.checkInvoiceStatus(order.getOrderCode())){
            System.out.println("franchiseCode is not equal or this order's status is true or delivery's status is not \"배송완료\" ");
            return false;
        }
        // 인수 완료 표시
        order.setOrderStatus(true);
        order.getOrderProductList().forEach(orderProduct->{
            if(requestPutOrder.getRequestProduct().get(orderProduct.getProduct().getProductCode())!=null) {
                int changeVal = requestPutOrder.getRequestProduct().get(orderProduct.getProduct().getProductCode());
                System.out.println("changeVal = " + changeVal);
                orderProduct.setRequestProductGetCount(changeVal);
                //검수 결과 가맹 창고에 저장
                franchiseWarehouseService.saveProduct(orderProduct.getProduct().getProductCode(), changeVal, orderProduct.getOrder().getFranchise().getFranchiseCode());

                orderProductRepository.save(orderProduct);
            }
        });
        return true;
    }
}
