package com.akatsuki.pioms.order.service;

import com.akatsuki.pioms.order.aggregate.*;

public interface OrderService {

    // 관리자가 관리하고 있는 모든 가맹점에 대한 발주들을 반환합니다.
    OrderListVO getFranchisesOrderList(int adminId);

    // 가맹점에서 새로운 발주를 생성합니다.
    // 생성을 정상적으로 한 경우 true를 반환합니다.
    // 비정상적인 생성인 경우: 신청한 상품이 본사 창고의 보유량보다 많은 경우. 발주서와 가맹점 코드가 불일치 하는 경우
    boolean postFranchiseOrder(int franchiseCode, RequestOrderVO order);

    // 관리자가 승인하지 않은 발주들을 반환합니다.
    OrderListVO getFranchisesUncheckedOrderList(int adminId);

    // 관리자가 승인대기 중인 발주를 승낙하여 결과를 String으로 반환힙니다.
    // 정상적으로 이루어진 경우: 관리자 코드와 주문코드의 관리자 코드가 일치. OrderCondition이 승인대기인 경우
    // 비정상적으로 이루어진 경우: 관리자 코드와 주문코드의 관리자 코드 불일치. OrderCondition이 승인 대기가 아닌 경우
    // 주문을 승낙할 때 발생되는 추가 로직
    // 1. 본사 창고 재고 수정
    // 2. 명세서 생성
    // 3. 송장 생성
    // 4. 가맹점의 반품신청 리스트 중 상태의 반품신청 추가
    String acceptOrder(int adminCOde,int orderId);

    // 관리자가 승인대기 중인 발주를 거절하여 결과를 String으로 반환합니다.
    // 정상적으로 이루어진 경우: 관리자 코드와 주문코드의 관리자 코드가 일치. OrderCondition이 승인대기인 경우
    // 비정상적으로 이루어진 경우: 관리자 코드와 주문코드의 관리자 코드 불일치. OrderCondition이 승인 대기가 아닌 경우
    String denyOrder(int adminCode,int orderId,String denyMessage);

    // 가맹점이 가맹점의 모든 발주 리스트를 반환합니다.
    OrderListVO getOrderList(int franchiseCode);

    // 가맹점이 발주서를 상세 조회합니다.
    // 정상적으로 이루어진 경우: 가맹점 코드와 주문의 가맹점 코드가 일치하는 경우
    // 비정상적으로 이루어진 경우: 가맹점 코드와 주문의 가맹점 코드가 일치하지 않는 경우
    OrderVO getOrder(int franchiseCode, int orderCode);

    // 관리자가 발주서를 상세 조회합니다.
    // 정상적으로 이루어진 경우: 관리자 코드와 주문의 관리자 코드가 일치하는 경우
    // 비정상적으로 이루어진 경우: 관리자 코드와 주문의 관리자 코드가 일치하지 않는 경우
    OrderVO getAdminOrder(int adminCode, int orderCode);

    // 가맹점이 발주서를 수정합니다.
    // 발주서 생성 결과를 boolean으로 반환합니다.
    // 발주서에 담겨있던 모든 발주상품들을 제거하고 요청온 상품들로 추가합니다.
    //
    // 발주가 정상적으로 생성되면 true, 비정상적인 경우 false를 반환합니다.
    // 정상적으로 이루어진 경우: 발주서와 가맹점 코드가 일치. 발주서 상태가 승인 대기인 경우.
    // 비정상적으로 이루어진 경우: 발주서와 가맹점 코드 불일치. 발주서 상태가 승인 대기가 아닌 경우
    boolean putFranchiseOrder(int franchiseCode, RequestPutOrder order);

    // 가맹점이 배송온 발주에 대해 검수한 결과를 저장합니다.
    // 각 발주서의 상품을 검수한 결과값을 RequestGetCount에 저장한 형태로 오며
    // 해당 값들을 가맹점 창고에 반영합니다.
    // 정상적으로 이루어진 경우
        // 가맹점 코드와 요청온 발주서의 가맹점 코드가 일치.
        // 주문의 isOrderStatus = false (검수를 진행했는지에 대한 여부)
        // 발주서의 상태가 배송완료가 완료인 경우
    // 비정상적으로 이루어진 경우
        // 가맹점 코드와 요청온 발주서의 가맹점 코드 불일치
        // 주문의 isOrderStatus = true
        // 발주의 상태가 배송완료가 아닌 경우
    // 관련된 추가 로직
    // 1. 가맹점 창고에 저장
    // 2. 만약 검수 온 상품이 본사와 불일치 하는 경우
        // 2-1. 적게 온 경우, 본사의 재고 +
        // 2-2. 많이 온  경우, 본사의 재고 -
    boolean putFranchiseOrderCheck(int franchiseCode, RequestPutOrderCheck requestPutOrder);
}
