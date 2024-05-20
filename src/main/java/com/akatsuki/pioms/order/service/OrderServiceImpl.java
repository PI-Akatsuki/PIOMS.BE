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
    public OrderDTO acceptOrder(int adminCode,int orderCode, ExchangeDTO exchange) {
        Order order = orderRepository.findById(orderCode).orElseThrow();

        if (!checkOrderCondition(order)) {
            System.out.println("order is null in checkOrderCondition");
            return null;
        }
        order.setOrderCondition(ORDER_CONDITION.승인완료);

        if (exchange!=null) {
            Exchange exchange1 = new Exchange();
            exchange1.setExchangeCode(exchange.getExchangeCode());
            order.setExchange(exchange1);
        }
        order.setOrderCondition(ORDER_CONDITION.검수대기);
        order=orderRepository.save(order);

        return new OrderDTO(order);
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
    public OrderDTO denyOrder(int adminCode,int orderCode, String denyMessage){
        Order order = orderRepository.findById(orderCode).orElse(null);
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
        Order order = new Order(ORDER_CONDITION.승인대기,false,franchise);
        Order result= orderRepository.save(order);
        result.setOrderProductList(new ArrayList<>());
        requestOrder.getProducts().forEach((productId, count)->{
            OrderProduct orderProduct = orderProductRepository.save(new OrderProduct(count,0, result, productId));
            result.getOrderProductList().add(orderProduct);
        });

        return new OrderDTO(result);
    }

    private static boolean checkOrderCondition(Order order) {
        if (order == null) {
            return false;
        }
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
        Order order = orderRepository.findById(requestOrder.getOrderCode())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getFranchise().getFranchiseCode() != franchiseCode) {
            return false;
        }

        if (order.getOrderCondition() == ORDER_CONDITION.승인완료) {
            return false;
        }
        // 기존 주문서의 상품 리스트 삭제
        orderProductRepository.deleteAllByOrderOrderCode(order.getOrderCode());

        // 주문서 상태 업데이트
        order.setOrderCondition(ORDER_CONDITION.승인대기);

        // 새로운 상품 리스트 추가
        requestOrder.getProducts().forEach((productId, count) -> {
            OrderProduct newOrderProduct = new OrderProduct(count, 0, order, productId);
            orderProductRepository.save(newOrderProduct);
        });

        return true;
    }

    @Override
    public boolean putFranchiseOrderCheck(int franchiseCode, RequestPutOrderCheck requestPutOrder) {
        return false;
    }

    @Override
    public boolean findOrderByExchangeCode(int exchangeCode) {
        return orderRepository.existsByExchangeExchangeCode(exchangeCode);
    }

    @Override
    public OrderDTO addExchangeToOrder(ExchangeDTO exchange, int orderCode) {
        Order order = orderRepository.findById(orderCode).orElseThrow();
        System.out.println("order = " + order);
        Exchange exchange1 = new Exchange();
        exchange1.setExchangeCode(exchange.getExchangeCode());
        order.setExchange(exchange1);
        
        order = orderRepository.save(order);
        return new OrderDTO(order);
    }

    @Override
    public OrderDTO putOrderCondition(int orderCode, ORDER_CONDITION orderCondition) {
        Order order = orderRepository.findById(orderCode).orElseThrow();
        order.setOrderCondition(orderCondition);
        return new OrderDTO(orderRepository.save(order));
    }

}
