package com.akatsuki.pioms.dashboard.aggregate;

import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.order.aggregate.OrderStat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseFranchiseDashBoard {
    private List<NoticeVO> noticeList;
    private AskListDTO askList;
    private OrderStat orderStat;
    private List<FranchiseWarehouseDTO> favoriteList;

    public ResponseFranchiseDashBoard(OrderStat orderStat, List<NoticeVO> notices, AskListDTO asks, List<FranchiseWarehouseDTO> favorites) {
        this.orderStat = orderStat;
        this.noticeList = notices;
        this.askList = asks;
        this.favoriteList = favorites;
    }
}
