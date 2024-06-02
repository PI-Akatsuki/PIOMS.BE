package com.akatsuki.pioms.dashboard.aggregate;

import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.company.aggregate.CompanyVO;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.order.aggregate.OrderStat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
public class ResponseAdminDashBoard {
    private CompanyVO companyVO;
    private OrderStat orderStat;
    private List<FranchiseDTO> franchiseList;
    private List<ExchangeDTO> exchangeList;
    private List<NoticeVO> noticeList;
    private AskListDTO askList;

    public ResponseAdminDashBoard(CompanyVO company, OrderStat orderStat, List<Franchise> franchises, List<ExchangeDTO> exchanges, List<NoticeVO> notices, AskListDTO asks) {
        this.companyVO = company;
        this.orderStat = orderStat;
        this.franchiseList = new ArrayList<>();
        for (int i = 0; i < franchises.size(); i++) {
            franchiseList.add(new FranchiseDTO(franchises.get(i)));
        }
        this.exchangeList = exchanges;
        this.noticeList = notices;
        this.askList = asks;
    }
}