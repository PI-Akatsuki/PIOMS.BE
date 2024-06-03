package com.akatsuki.pioms.dashboard.service;

import com.akatsuki.pioms.admin.service.AdminInfoService;
import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.ask.service.AskService;
import com.akatsuki.pioms.company.aggregate.CompanyVO;
import com.akatsuki.pioms.company.service.CompanyService;
import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.dashboard.aggregate.ResponseAdminDashBoard;
import com.akatsuki.pioms.dashboard.aggregate.ResponseDriverDashBoard;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.repository.DeliveryDriverRepository;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.repository.FranchiseRepository;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.frowner.service.FranchiseOwnerService;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.service.NoticeService;
import com.akatsuki.pioms.order.aggregate.OrderStat;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.service.OrderService;
import jakarta.transaction.Transactional;
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
    private final FranchiseOwnerService franchiseOwnerService;
    private final InvoiceService invoiceService;

    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;
    @Autowired
    private FranchiseRepository franchiseRepository;
    @Autowired
    private FranchiseOwnerRepository franchiseOwnerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    public DashboardServiceImpl(GetUserInfo getUserInfo, FranchiseService franchiseService, AdminInfoService adminInfoService, OrderService orderService, ExchangeService exchangeService, CompanyService companyService, NoticeService noticeService, AskService askService, FranchiseOwnerService franchiseOwnerService,InvoiceService invoiceService) {
        this.getUserInfo = getUserInfo;
        this.franchiseService = franchiseService;
        this.adminInfoService = adminInfoService;
        this.orderService = orderService;
        this.exchangeService = exchangeService;
        this.companyService = companyService;
        this.noticeService = noticeService;
        this.askService = askService;
        this.franchiseOwnerService = franchiseOwnerService;
        this.invoiceService = invoiceService;
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

    @Override
    @Transactional
    public ResponseDriverDashBoard getDriverDashBoard() {

        int driverCode = getUserInfo.getDriverCode();

        DeliveryDriver deliveryDriver = deliveryDriverRepository.findById(driverCode)
                .orElseThrow(() -> new RuntimeException("배송기사를 찾을 수 없습니다: " + driverCode));

        // Franchise 정보 가져오기
        Franchise franchise = franchiseRepository.findAllByDeliveryDriverDriverCode(driverCode)
                .stream().findFirst().orElseThrow(() -> new RuntimeException("프랜차이즈를 찾을 수 없습니다: " + driverCode));
        FranchiseDTO franchiseDTO = new FranchiseDTO(franchise);

        // FranchiseOwner 정보 가져오기
        FranchiseOwner franchiseOwner = franchiseOwnerRepository.findByFranchiseOwnerId(franchise.getFranchiseOwner().getFranchiseOwnerId())
                .orElseThrow(() -> new RuntimeException("프랜차이즈 주인을 찾을 수 없습니다: " + franchise.getFranchiseOwner().getFranchiseOwnerId()));
        FranchiseOwnerDTO franchiseOwnerDTO = new FranchiseOwnerDTO(franchiseOwner);

        List<OrderDTO> orderDTOs = orderService.getOrderListByDriverCode(driverCode);
        List<InvoiceDTO> invoiceDTO = invoiceService.getInvoicesByDriverCode(driverCode);

        return new ResponseDriverDashBoard(deliveryDriver, franchiseDTO, franchiseOwnerDTO, orderDTOs, invoiceDTO);
    }

}
