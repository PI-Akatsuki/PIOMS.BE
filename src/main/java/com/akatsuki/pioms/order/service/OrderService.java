package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.order.aggregate.*;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;

import java.util.List;

public interface OrderService {

    /**
     * @CopyRight 32173594@dankook.ac.kr
     * <br><br><br>
     * <h1>Order Service</h1>
     *     <br>
     * <h1>Admin Functions</h1>
     * <h2>1. Get Order List By Admin Code : 관리자가 관리하고 있는 모든 가맹점에 대한 발주 목록 반환 </h2>
     * <h2>2. Get Admin Unchecked Orders : 관리자가 처리하지 않은 발주 목록 반환 </h2>
     * <h2>3. Accept Order : 관리자가 발주를 승인 </h2>
     * <h2>4. Deny Order : 관리자가 발주를 거절 </h2>
     * <h2>5. Get Admin Order : 관리자가 발주를 상세 조회 </h2>
     * <br>
     *
     * <h1> Franchise Owner Functions</h1>
     * <h2>1. Post Franchise Order : 점주가 새로운 발주 생성 </h2>
     * <h2>2. Get Order List : 점주의 모든 발주 반환</h2>
     * <h2>3. Get Order : 점주의 발주 상세 조회</h2>
     * <h2>4. Put Franchise Order : 주문 수정 </h2>
     * <h2>5. Put Franchise Order Check : 배송 온 발주의 검수 로직</h2>
     * <br>
     *
     * <h1> INNER FUNCTIONS </h1>
     * <h2>1. Add Exchange To Order : 관리자가 승인을 하면서 주문에 요청 온 교환을 발주에 추가  </h2>
     * <h2>2. Check Product Cnt : 물건 보낼 수 있는지 확인하기 위함 </h2>
     * <h2>3. Find Order By Exchange Code : 반송 코드를 통해 주문  </h2>
     * <h2>4. Add Exchange To Order : 주문에 교환 코드 추가 </h2>
     * */

    // 관리자가 관리하고 있는 모든 가맹점에 대한 발주들을 반환합니다.
    List<OrderDTO> getOrderListByAdminCode(int adminId);

    // 가맹점에서 새로운 발주를 생성합니다.
    // 생성을 정상적으로 한 경우 true를 반환합니다.
    // 비정상적인 생성인 경우: 신청한 상품이 본사 창고의 보유량보다 많은 경우. 발주서와 가맹점 코드가 불일치 하는 경우
    int postFranchiseOrder(FranchiseDTO franchise, RequestOrderVO order,int price);

    // 관리자가 승인대기 중인 발주를 승낙하여 결과를 String으로 반환힙니다.
    // 정상적으로 이루어진 경우: 관리자 코드와 주문코드의 관리자 코드가 일치. OrderCondition이 승인대기인 경우
    // 비정상적으로 이루어진 경우: 관리자 코드와 주문코드의 관리자 코드 불일치. OrderCondition이 승인 대기가 아닌 경우
    // 주문을 승낙할 때 발생되는 추가 로직
    // 1. 본사 창고 재고 수정
    // 2. 명세서 생성
    // 3. 송장 생성
    // 4. 가맹점의 반품신청 리스트 중 상태의 반품신청 추가
    OrderDTO acceptOrder(int adminCOde,int orderId, ExchangeDTO exchange);

    // 관리자가 승인대기 중인 발주를 거절하여 결과를 String으로 반환합니다.
    // 정상적으로 이루어진 경우: 관리자 코드와 주문코드의 관리자 코드가 일치. OrderCondition이 승인대기인 경우
    // 비정상적으로 이루어진 경우: 관리자 코드와 주문코드의 관리자 코드 불일치. OrderCondition이 승인 대기가 아닌 경우
    int denyOrder(int adminCode,int orderId,String denyMessage);

    // 가맹점이 가맹점의 모든 발주 리스트를 반환합니다.
    List<OrderDTO> getOrderListByFranchiseOwnerCode(int franchiseCode);

    // 가맹점이 발주서를 상세 조회합니다.
    // 정상적으로 이루어진 경우: 가맹점 코드와 주문의 가맹점 코드가 일치하는 경우
    // 비정상적으로 이루어진 경우: 가맹점 코드와 주문의 가맹점 코드가 일치하지 않는 경우
    OrderDTO getOrder(int franchiseCode, int orderCode);

    // 관리자가 발주서를 상세 조회합니다.
    // 정상적으로 이루어진 경우: 관리자 코드와 주문의 관리자 코드가 일치하는 경우
    // 비정상적으로 이루어진 경우: 관리자 코드와 주문의 관리자 코드가 일치하지 않는 경우
    OrderDTO getDetailOrderByAdminCode(int adminCode, int orderCode);

    // 가맹점이 발주서를 수정합니다.
    // 발주서 생성 결과를 boolean으로 반환합니다.
    // 발주서에 담겨있던 모든 발주상품들을 제거하고 요청온 상품들로 추가합니다.
    //
    // 발주가 정상적으로 생성되면 true, 비정상적인 경우 false를 반환합니다.
    // 정상적으로 이루어진 경우: 발주서와 가맹점 코드가 일치. 발주서 상태가 승인 대기인 경우.
    // 비정상적으로 이루어진 경우: 발주서와 가맹점 코드 불일치. 발주서 상태가 승인 대기가 아닌 경우
    boolean putFranchiseOrder(int franchiseOwnerCode, RequestPutOrder order, int price);

    boolean findOrderByExchangeCode(int exchangeCode);

    OrderDTO putOrderCondition(int orderCode, ORDER_CONDITION orderCondition);

    OrderDTO getOrderById(int orderCode);

    OrderStat getOrderStat(int rootCode);

    boolean findUnprocessedOrder(int franchiseOwnerCode);

    List<OrderDTO> getOrderListByDriverCode(int driverCode);
}
