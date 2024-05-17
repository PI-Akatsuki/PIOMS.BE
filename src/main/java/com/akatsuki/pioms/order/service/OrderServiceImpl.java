package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.aggregate.Exchange;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.order.aggregate.*;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.repository.OrderProductRepository;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    OrderRepository orderRepository;
    OrderProductRepository orderProductRepository;


//    ExchangeService exchangeService;
//    ProductService productService;
//    InvoiceService invoiceService;
//    FranchiseWarehouseService franchiseWarehouseService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrderListByAdminCode(int adminCode){

        List<Order> orderList;
        if (adminCode==1){
            // 루트 관리자는 전부
            orderList = orderRepository.findAll();
        }else
            orderList = orderRepository.findAllByFranchiseAdminAdminCode(adminCode);

        if (orderList == null || orderList.isEmpty())
            return null;

        List<Order> orderDTOList = new ArrayList<>();

        orderDTOList.addAll(orderList);

        return orderDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAdminUncheckesOrders(int adminCode){
        List<Order> orderList;

        if (adminCode == 1){
            orderList = orderRepository.findAllByOrderCondition(ORDER_CONDITION.승인대기);
        }else
            orderList = orderRepository.findAllByFranchiseAdminAdminCodeAndOrderCondition(adminCode, ORDER_CONDITION.승인대기);
        if (orderList == null || orderList.isEmpty())
            return null;

        List<OrderDTO> orderDTOList = new ArrayList<>();

        orderList.forEach(order-> {
            orderDTOList.add(new OrderDTO(order));
        });

        return orderDTOList;
    }

    @Override
    @Transactional(readOnly = false)
    public Order acceptOrder(int adminCode,int orderId, ExchangeDTO exchange) {
        System.out.println("acceptOrder");
        Order order = orderRepository.findById(orderId).orElseThrow();

        if (!checkOrderCondition(order))
            return null;
        order.setOrderCondition(ORDER_CONDITION.승인완료);

        if (exchange!=null) {
            Exchange exchange1 = new Exchange();
            exchange1.setExchangeCode(exchange.getExchangeCode());
            order.setExchange(exchange1);
        }

        order=orderRepository.save(order);
        return order;
    }

    @Override
    public boolean checkProductCnt(OrderDTO order) {
        for (int i = 0; i < order.getOrderProductList().size(); i++) {
            if(order.getOrderProductList().get(i).getRequestProductCount() > order.getOrderProductList().get(i).getRequestProductCount())
                return false;
        }
        return true;
    }

    @Override
    @Transactional
    public OrderDTO denyOrder(int adminCode,int orderId, String denyMessage){
        Order order = orderRepository.findById(orderId).orElse(null);
        if(order==null||order.getFranchise().getAdmin().getAdminCode() != adminCode || !checkOrderCondition(order)){
            return null;
        }

        order.setOrderCondition(ORDER_CONDITION.승인거부);
        order.setOrderReason(denyMessage);
        order = orderRepository.save(order);
        return new OrderDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO postFranchiseOrder(Franchise franchise, RequestOrderVO requestOrder){
        if(franchise.getFranchiseCode() != requestOrder.getFranchiseCode()){
            System.out.println("가맹점 코드, 주문의 가맹점 코드 불일치! ");
            return null;
        }
        // 이미 존재하는 발주 있는지 확인
        if (orderRepository.existsByFranchiseFranchiseCodeAndOrderCondition(franchise.getFranchiseCode(), ORDER_CONDITION.승인대기)
                || orderRepository.existsByFranchiseFranchiseCodeAndOrderCondition(franchise.getFranchiseCode(),ORDER_CONDITION.승인거부)){
            System.out.println("이미 대기중인 발주가 존재합니다.");
            return null;
        }
        // 발주 생성
//        Franchise franchise = new Franchise();
//        franchise.setFranchiseCode(requestOrder.getFranchiseCode());

        Order order = new Order(ORDER_CONDITION.승인대기,false,franchise);
        order= orderRepository.save(order);

        int orderId = order.getOrderCode();
        // 발주 상품 저장
        requestOrder.getProducts().forEach((productId, count)->{
            Order order1 = orderRepository.findById(orderId).orElseThrow();
            orderProductRepository.save(new OrderProduct(count,0, order1, productId));
        });
        return new OrderDTO(order);
    }

    private static boolean checkOrderCondition(Order order) {
        if (order == null)
            return false;
        if(order.getOrderCondition() == ORDER_CONDITION.승인대기)
            return true;
        return false;
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrderList(int franchiseCode){
        List<Order> orderList= orderRepository.findByFranchiseFranchiseCode(franchiseCode);
        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderList.forEach(order-> {
            orderDTOList.add(new OrderDTO(order));
        });
        return orderDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(int franchiseCode,int orderCode){
        Order order = orderRepository.findById(orderCode).orElseThrow();
        if(franchiseCode!= order.getFranchise().getFranchiseCode()){
            System.out.println("가맹점 코드, 주문의 가맹점 코드 불일치!");
            return null;
        }
        System.out.println("order = " + order);
        System.out.println(order.getOrderProductList());
        return new OrderDTO(order);
    }

    @Override
    public OrderDTO getAdminOrder(int adminCode, int orderCode) {
        Order order = orderRepository.findById(orderCode).orElse(null);

        if(order==null || adminCode != order.getFranchise().getAdmin().getAdminCode()){
            return null;
        }

        return new OrderDTO(order);
    }

    @Override
    @Transactional
    public boolean putFranchiseOrder(int franchiseCode, RequestPutOrder requestOrder) {
        Order order = orderRepository.findById(requestOrder.getOrderCode()).orElseThrow(IllegalArgumentException::new);
        System.out.println("order = " + order);
        if(order.getFranchise().getFranchiseCode() != franchiseCode) {
            System.out.println("프랜차이즈 코드 불일치");
            return false;
        }
        if (order.getOrderCondition() == ORDER_CONDITION.승인완료) {
            System.out.println("이미 처리된 발주");
            return false;
        }
        orderProductRepository.deleteAllByOrderOrderCode(order.getOrderCode());

        Order deletedorder = orderRepository.findById(requestOrder.getOrderCode()).orElseThrow(IllegalArgumentException::new);
        deletedorder.setOrderCondition(ORDER_CONDITION.승인대기);
        requestOrder.getProducts().forEach((productId, count)->{
            orderProductRepository.save(new OrderProduct(count,0, deletedorder, productId));
        });
        System.out.println(deletedorder.getOrderProductList());
        orderRepository.save(deletedorder);
        return true;
    }

    @Override
    @Transactional
    public boolean putFranchiseOrderCheck(int franchiseCode, RequestPutOrderCheck requestPutOrder) {
        Order order = orderRepository.findById(requestPutOrder.getOrderCode()).orElseThrow(IllegalArgumentException::new);
        System.out.println("requestPutOrder = " + requestPutOrder);
        if(franchiseCode != order.getFranchise().getFranchiseCode() || order.isOrderStatus()
//                || !invoiceService.checkInvoiceStatus(order.getOrderCode())
        ){
            System.out.println("franchiseCode is not equal or this order's status is true or delivery's status is not \"배송완료\" ");
            return false;
        }
        // 인수 완료 표시
        order.setOrderStatus(true);
        order.getOrderProductList().forEach(orderProduct->{
            if(requestPutOrder.getRequestProduct().get(orderProduct.getProduct().getProductCode())!=null) {
                int changeVal = requestPutOrder.getRequestProduct().get(orderProduct.getProduct().getProductCode());
                int requestVal = orderProduct.getRequestProductCount();

                orderProduct.setRequestProductGetCount(changeVal);
                //검수 결과 가맹 창고에 저장
//                franchiseWarehouseService.saveProduct(orderProduct.getProduct().getProductCode(), changeVal, orderProduct.getOrder().getFranchise().getFranchiseCode());
                if(changeVal != requestVal){
//                    productService.editIncorrectCount(orderProduct.getProduct(), requestVal-changeVal);
                }
                orderProductRepository.save(orderProduct);
            }
        });

//        franchiseWarehouseService.saveExchangeProduct(order.getExchange(), franchiseCode);

        return true;
    }

    @Override
    public boolean findOrderByExchangeCode(int exchangeCode) {
        return orderRepository.existsByExchangeExchangeCode(exchangeCode);
    }

    @Override
    public OrderDTO addExchangeToOrder(ExchangeDTO exchange, int orderCode) {
        Order order = orderRepository.findById(orderCode).orElseThrow();
        Exchange exchange1 = new Exchange();
        exchange1.setExchangeCode(exchange.getExchangeCode());
        order.setExchange(exchange1);
        order = orderRepository.save(order);
        return new OrderDTO(order);
    }

}
