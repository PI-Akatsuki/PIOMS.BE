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
import java.util.Optional;

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
    public List<OrderDTO> getOrderListByAdminCode(int adminCode) {
        List<Order> orderList;
        if (adminCode == 1) {
            // 루트 관리자는 전부
            orderList = orderRepository.findAllDesc();
            System.out.println("find all orderList = " + orderList);
        } else {
            orderList = orderRepository.findAllByFranchiseAdminAdminCodeOrderByOrderDateDesc(adminCode);
        }
        System.out.println("orderList = " + orderList);
        if (orderList == null || orderList.isEmpty()) {
            return null;
        }
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (Order order : orderList) {
            orderDTOList.add(new OrderDTO(order));
        }
        return orderDTOList;
    }


    @Override
    @Transactional(readOnly = false)
    public OrderDTO acceptOrder(int adminCode, int orderCode, ExchangeDTO exchange) {
        Order order = orderRepository.findById(orderCode)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with code: " + orderCode));
        order.setOrderCondition(ORDER_CONDITION.승인완료);
        if (exchange != null) {
            Exchange exchange1 = new Exchange();
            exchange1.setExchangeCode(exchange.getExchangeCode());
            order.setExchange(exchange1);
        }
        order = orderRepository.save(order);
        System.out.println("order = " + order);
        return new OrderDTO(order);
    }

    @Override
    @Transactional(readOnly = false)
    public int denyOrder(int adminCode,int orderCode, String denyMessage){
        Order order = orderRepository.findById(orderCode).orElse(null);
        if(order==null||
                order.getOrderCondition()!=ORDER_CONDITION.승인대기){
            return 0;
        }
        order.setOrderCondition(ORDER_CONDITION.승인거부);
        order.setOrderReason(denyMessage);
        orderRepository.save(order);
        return 1;
    }

    @Override
    @Transactional(readOnly = false)
    public int postFranchiseOrder(FranchiseDTO franchiseDTO, RequestOrderVO requestOrder, int price){

        // 이미 존재하는 발주 있는지 확인
        if (requestOrder.getProducts() == null || requestOrder.getProducts().isEmpty() ||
                orderRepository.existsByFranchiseFranchiseCodeAndOrderCondition(franchiseDTO.getFranchiseCode(), ORDER_CONDITION.승인대기)
                || orderRepository.existsByFranchiseFranchiseCodeAndOrderCondition(franchiseDTO.getFranchiseCode(),ORDER_CONDITION.승인거부)
        ){
            return 0;
        }
        try {
            postOrder(franchiseDTO, requestOrder, price);
            return 1;
        }
        catch (Exception e){
            System.err.println("post failed");
            return -1;
        }
    }
    private void postOrder(FranchiseDTO franchiseDTO, RequestOrderVO requestOrder,int price) {
        Order order = new Order(ORDER_CONDITION.승인대기, franchiseDTO);
        order.setOrderTotalPrice(price);
        Order result = orderRepository.save(order);
        requestOrder.getProducts().forEach((productCode, count) -> {
            orderProductRepository.save(new OrderProduct(count, 0, result, productCode));
        });
    }


    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrderListByFranchiseOwnerCode(int franchiseOwnerCode){
        List<Order> orderList= orderRepository.findByFranchiseFranchiseOwnerFranchiseOwnerCodeOrderByOrderDateDesc(franchiseOwnerCode);
        if (orderList==null||orderList.isEmpty()){
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
    public OrderDTO getDetailOrderByAdminCode(int adminCode, int orderCode) {
        Order order = orderRepository.findById(orderCode).orElse(null);
        if(order==null || adminCode != order.getFranchise().getAdmin().getAdminCode()){
            return null;
        }

        return new OrderDTO(order);
    }

    @Override
    @Transactional
    public boolean putFranchiseOrder(int franchiseOwnerCode, RequestPutOrder requestOrder, int price) {
        Order order = orderRepository.findById(requestOrder.getOrderCode())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (order.getFranchise().getFranchiseOwner().getFranchiseOwnerCode() != franchiseOwnerCode) {
            return false;
        }
        if (order.getOrderCondition() != ORDER_CONDITION.승인대기
                && order.getOrderCondition() != ORDER_CONDITION.승인거부 ) {
            return false;
        }
        order.setOrderTotalPrice(price);
        putOrder(requestOrder, order);
        return true;
    }

    @Transactional
    public void putOrder(RequestPutOrder requestOrder, Order order) {
        // 기존 주문서의 상품 리스트 삭제
        orderProductRepository.deleteAllByOrderOrderCode(order.getOrderCode());
        // 주문서 상태 업데이트
        order.setOrderCondition(ORDER_CONDITION.승인대기);

        // 새로운 상품 리스트 추가
        requestOrder.getProducts().forEach((productCode, count) -> {
            OrderProduct newOrderProduct = new OrderProduct(count, 0, order, productCode);
            orderProductRepository.save(newOrderProduct);
        });
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

    @Override
    public OrderStat getOrderStat(int adminCode) {
        if(adminCode==1){
            int waitCnt = orderRepository.getWaitCnt();
            int acceptCnt = orderRepository.getAcceptCnt();
            int denyCnt = orderRepository.getDenyCnt();
            int deliveryCnt = orderRepository.getDeliveryCnt();
            int inspectionWaitCnt = orderRepository.getInspectionWaitCnt();
            int inspectionFinishCnt = orderRepository.getInspectionFinishCnt();
            return new OrderStat(waitCnt,acceptCnt,denyCnt,deliveryCnt,inspectionWaitCnt,inspectionFinishCnt);
        }
        int waitCnt = orderRepository.getWaitCntByAdminCode(adminCode);
        int acceptCnt = orderRepository.getAcceptCntByAdminCode(adminCode);
        int denyCnt = orderRepository.getDenyCntByAdminCode(adminCode);
        int deliveryCnt = orderRepository.getDeliveryCntByAdminCode(adminCode);
        int inspectionWaitCnt = orderRepository.getInspectionWaitCntByAdminCode(adminCode);
        int inspectionFinishCnt = orderRepository.getInspectionFinishCntByAdminCode(adminCode);
        return new OrderStat(waitCnt,acceptCnt,denyCnt,deliveryCnt,inspectionWaitCnt,inspectionFinishCnt);
    }

    @Override
    public boolean findUnprocessedOrder(int franchiseOwnerCode) {
        // 발주 생성 전 미처리된 발주 있는지 확인 - 미처리 있으면 true
         int result = orderRepository.findUnprocessedOrder(franchiseOwnerCode);
        System.out.println("result = " + result);
        return result != 0;
    }

    @Override
    public List<OrderDTO> getOrderListByDriverCode(int driverCode) {
        List<Order> orders = orderRepository.findAllByFranchiseDeliveryDriverDriverCode(driverCode);
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            orderDTOList.add(new OrderDTO(orders.get(i)));
        }
        return orderDTOList;
    }


}
