package com.akatsuki.pioms.exchange.service;

import com.akatsuki.pioms.exchange.aggregate.*;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;

import java.util.List;

public interface ExchangeService {

    // 관리자가 발주를 승인하고 가맹점의 반품 신청이 있는지 조회하고 반품 신청 상태의 반품을 발주서에 담기 위한 로직
    // 만약 가맹점의 발주 신청 상태의 발주 리스트가 중복되어 여러개가 있는 경우 에러로 인식하여 해당 반품신청들을 삭제한다.
    ExchangeDTO findExchangeToSend(int franchiseCode);

    // 모든 반품 신청 리스트를 조회하기 위한 로직
    List<ExchangeDTO> getExchanges();

    // 한 가맹점의 모든 반품 조회하기 위한 로직
    List<ExchangeDTO> getExchangesByFranchiseCode(int franchiseCode);

    // 관리자가 관리하고 있는 모든 가맹점의 반품 리스트를 조회하기 위한 로직
    List<ExchangeDTO> getExchangesByAdminCode(int adminCode);

    ExchangeDTO putExchange(int exchangeCode, RequestExchange requestExchange);

    ExchangeDTO postExchange(int franchiseCode, RequestExchange requestExchange);

    List<ExchangeProduct> getExchangeProducts(int exchangeCode);

    List<ExchangeProduct> getExchangeProductsWithStatus(int exchangeCode, EXCHANGE_PRODUCT_STATUS exchangeProductStatus);
}
