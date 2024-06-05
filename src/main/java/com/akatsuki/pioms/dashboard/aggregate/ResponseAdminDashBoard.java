package com.akatsuki.pioms.dashboard.aggregate;

import com.akatsuki.pioms.ask.dto.AskDTO;
import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.company.aggregate.CompanyVO;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.order.aggregate.OrderStat;
import com.akatsuki.pioms.product.dto.ProductDTO;
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
    private List<AskDTO> askList;
    private List<ProductDTO> products;


    public ResponseAdminDashBoard(CompanyVO company, OrderStat orderStat,
                                  List<FranchiseDTO> franchises, List<ExchangeDTO> exchanges,
                                  List<NoticeVO> notices, List<AskDTO> asks,
                                  List<ProductDTO> products
    ) {
        this.companyVO = company;
        this.orderStat = orderStat;
        this.franchiseList = franchises;
        this.exchangeList = exchanges;
        this.noticeList = notices;
        this.askList = asks;
        if (products!=null||!products.isEmpty())
            this.products=products;
    }

    public ResponseAdminDashBoard(CompanyVO company, OrderStat orderStat,
                                  List<FranchiseDTO> franchises, List<ExchangeDTO> exchanges,
                                  List<NoticeVO> notices, List<AskDTO> asks) {
        this.companyVO = company;
        this.orderStat = orderStat;
        this.franchiseList =  franchises;
        this.exchangeList = exchanges;
        this.noticeList = notices;
        this.askList = asks;
    }
}
