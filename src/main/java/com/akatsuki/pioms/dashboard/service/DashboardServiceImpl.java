package com.akatsuki.pioms.dashboard.service;

import com.akatsuki.pioms.admin.service.AdminInfoService;
import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.ask.service.AskService;
import com.akatsuki.pioms.company.aggregate.CompanyVO;
import com.akatsuki.pioms.company.service.CompanyService;
import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.dashboard.aggregate.ResponseAdminDashBoard;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.service.NoticeService;
import com.akatsuki.pioms.order.aggregate.OrderStat;
import com.akatsuki.pioms.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DashboardServiceImpl implements DashboardService {
    private final GetUserInfo getUserInfo;
    private final FranchiseService franchiseService;
    private final AdminInfoService adminInfoService;
    private final OrderService orderService;
    private final ExchangeService exchangeService;
    private final CompanyService companyService;
    private final NoticeService noticeService;
    private final AskService askService;

    @Autowired
    public DashboardServiceImpl(GetUserInfo getUserInfo, FranchiseService franchiseService, AdminInfoService adminInfoService, OrderService orderService, ExchangeService exchangeService, CompanyService companyService, NoticeService noticeService, AskService askService) {
        this.getUserInfo = getUserInfo;
        this.franchiseService = franchiseService;
        this.adminInfoService = adminInfoService;
        this.orderService = orderService;
        this.exchangeService = exchangeService;
        this.companyService = companyService;
        this.noticeService = noticeService;
        this.askService = askService;
    }


    @Override
    public ResponseAdminDashBoard getRootDash(){

        int rootCode = getUserInfo.getAdminCode();
        CompanyVO company = companyService.findInformation();
        OrderStat orderStat = orderService.getOrderStat(rootCode);
        List<Franchise> franchises = franchiseService.findFranchiseList();
        List<ExchangeDTO> exchanges = exchangeService.getExchangesByAdminCode();
        List<NoticeVO> notices = noticeService.getAllNoticeList();
        AskListDTO asks = askService.getAllAskList();

        System.out.println("company = " + company);
        System.out.println("orderStat = " + orderStat);
        System.out.println("franchises = " + franchises);
        System.out.println("exchanges = " + exchanges);
        System.out.println("notices = " + notices);
        System.out.println("asks = " + asks);
        return new ResponseAdminDashBoard(company,orderStat,franchises,exchanges,notices,asks);
    }

    @Override
    public ResponseAdminDashBoard getAdminDash(){

        int adminCode = getUserInfo.getAdminCode();

        CompanyVO company = companyService.findInformation();
        OrderStat orderStat = orderService.getOrderStat(adminCode);
        List<Franchise> franchises = franchiseService.findFranchiseByAdminCode(adminCode);
        List<ExchangeDTO> exchanges = exchangeService.getExchangesByAdminCode();
        List<NoticeVO> notices = noticeService.getAllNoticeList();
        AskListDTO asks = askService.getAllAskList();

        System.out.println("company = " + company);
        System.out.println("orderStat = " + orderStat);
        System.out.println("franchises = " + franchises);
        System.out.println("exchanges = " + exchanges);
        System.out.println("notices = " + notices);
        System.out.println("asks = " + asks);
        return new ResponseAdminDashBoard(company,orderStat,franchises,exchanges,notices,asks);
    }

}
