package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.aggregate.Exchange;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.order.aggregate.*;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.repository.OrderProductRepository;
import com.akatsuki.pioms.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

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
            orderList = orderRepository.findAllByFranchiseAdminAdminCodeOrderByOrderDateDesc(adminCode);
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
            orderList = orderRepository.findAllByOrderConditionOrderByOrderDateDesc(ORDER_CONDITION.승인대기);
        }else
            orderList = orderRepository.findAllByFranchiseAdminAdminCodeAndOrderConditionOrderByOrderDateDesc(adminCode, ORDER_CONDITION.승인대기);
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
        order.setOrderCondition(ORDER_CONDITION.승인완료);

        if (exchange!=null) {
            Exchange exchange1 = new Exchange();
            exchange1.setExchangeCode(exchange.getExchangeCode());
            order.setExchange(exchange1);
        }

        order=orderRepository.save(order);
        return new OrderDTO(order);
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
    public OrderDTO postFranchiseOrder(FranchiseDTO franchiseDTO, RequestOrderVO requestOrder){
        if(franchiseDTO.getFranchiseCode() != requestOrder.getFranchiseCode()){
            System.out.println("가맹점 코드, 주문의 가맹점 코드 불일치! ");
            return null;
        }

        // 이미 존재하는 발주 있는지 확인
        if (orderRepository.existsByFranchiseFranchiseCodeAndOrderCondition(franchiseDTO.getFranchiseCode(), ORDER_CONDITION.승인대기)
                || orderRepository.existsByFranchiseFranchiseCodeAndOrderCondition(franchiseDTO.getFranchiseCode(),ORDER_CONDITION.승인거부)){
            System.out.println("이미 대기중인 발주가 존재합니다.");
            return null;
        }
        System.out.println("22");

        // 발주 생성
        Order order = new Order(ORDER_CONDITION.승인대기,franchiseDTO);
        order.setOrderTotalPrice(requestOrder.getOrderTotalPrice());
        Order result= orderRepository.save(order);
        System.out.println("result = " + result);
        // 발주 상품 저장
        result.setOrderProductList(new ArrayList<>());
        requestOrder.getProducts().forEach((productId, count)->{
            OrderProduct orderProduct = orderProductRepository.save(new OrderProduct(count,0, result, productId));
            result.getOrderProductList().add(orderProduct);
        });
        return new OrderDTO(result,franchiseDTO);
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
    public List<OrderDTO> getOrderList(int franchiseOwnerCode){
        List<Order> orderList= orderRepository.findByFranchiseFranchiseOwnerFranchiseOwnerCodeOrderByOrderDateDesc(franchiseOwnerCode);
        if (orderList.isEmpty()){
            return Collections.emptyList();
        }
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
        System.out.println("order = " + order);
        if(order==null || adminCode != order.getFranchise().getAdmin().getAdminCode()){
            System.out.println("adminCode = " + adminCode);
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
    public boolean findOrderByExchangeCode(int exchangeCode) {
        return orderRepository.existsByExchangeExchangeCode(exchangeCode);
    }



    @Override
    public OrderDTO putOrderCondition(int orderCode, ORDER_CONDITION orderCondition) {
        Order order = orderRepository.findById(orderCode).orElseThrow();
        order.setOrderCondition(orderCondition);
        return new OrderDTO(orderRepository.save(order));
    }

    @Override
    public OrderDTO getOrderById(int orderCode) {
        Order order;
        try {
            order=orderRepository.findById(orderCode).orElseThrow();
            return new OrderDTO(order);
        }catch (Exception ignored){
            return null;
        }
    }


}
