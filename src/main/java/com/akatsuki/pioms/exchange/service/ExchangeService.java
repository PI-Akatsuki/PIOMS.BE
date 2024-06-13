package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.aggregate.*;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.order.dto.OrderDTO;

import java.util.List;

public interface ExchangeService {

    /**
     * .@Copyright 32173594@dankook.ac.kr
     * <br><br><br>
     * <h1>Exchange Service</h1>
     *     <br>
     * <h1>Admin Functions</h1>
     * <h2>1. Get Exchanges By Admin Code : 관리자가 관리하는 가맹점들의 반송 신청 목록 조회 </h2>
     * <h2>2. Get Admin Exchange 관리자가 관리하는 반송 상세 조회</h2>
     * <h2>3. Put Exchange : 본사로 도착한 반송 물품을 검수하기 위한 로직 </h2>
     * <br>
     *
     * <h1> Franchise Owner Functions</h1>
     * <h2>1. Get Fr-Owner Exchanges: 점주의 반송 목록 조회</h2>
     * <h2>2. Get Exchanges By Franchise Code : 가맹점의 반송 상세 조회</h2>
     * <h2>3. Post Exchange : 반송 등록 로직 </h2>
     * <h2>4. Delete Exchange : 아직 처리 되지 않은 반송을 취소하는 로직</h2>
     * <br>
     *
     * <h1> INNER FUNCTIONS </h1>
     * <h2>1. Get Exchange Products With Status : 반품/교환 반송 물품들 조회 로직 </h2>
     * <h2>1. Find Exchange To Send : 본사로 전송할 반송을 찾기 위한 로직 </h2>
     * */


    // 모든 반품 신청 리스트를 조회하기 위한 로직
    List<ExchangeDTO> getExchangesByAdminCode();
    ExchangeDTO processArrivedExchange(int exchangeCode, RequestExchange requestExchange);
    ExchangeDTO getExchangeByAdminCode(int exchangeCode);

    // 한 가맹점의 모든 반품 조회하기 위한 로직
    // 관리자가 관리하고 있는 모든 가맹점의 반품 리스트를 조회하기 위한 로직
    ExchangeDTO getExchangeByFranchiseOwnerCode(int exchangeCode);
    ExchangeDTO postExchange(RequestExchange requestExchange);
    List<ExchangeDTO> getFrOwnerExchanges();
    boolean deleteExchange(int exchangeCode);
    // 관리자가 발주를 승인하고 가맹점의 반품 신청이 있는지 조회하고 반품 신청 상태의 반품을 발주서에 담기 위한 로직
    // 만약 가맹점의 발주 신청 상태의 발주 리스트가 중복되어 여러개가 있는 경우 에러로 인식하여 해당 반품신청들을 삭제한다.
    ExchangeDTO findExchangeToSend(int franchiseCode);

    boolean updateExchangeEndDelivery(int franchiseCode);

    void updateExchangeStartDelivery(int franchiseCode);

    void updateExchangeToCompany(int exchangeCode);

    void afterAcceptOrder(OrderDTO order);

    List<ExchangeDTO> findExchangeInDeliveryCompanyToFranchise(int franchiseOwnerCode);

    List<ExchangeDTO> getExchangesByAdminCodeOrderByExchangeDateDesc();
}
