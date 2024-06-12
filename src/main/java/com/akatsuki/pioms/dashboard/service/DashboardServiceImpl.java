package com.akatsuki.pioms.dashboard.service;

import com.akatsuki.pioms.admin.service.AdminInfoService;
import com.akatsuki.pioms.ask.dto.AskDTO;
import com.akatsuki.pioms.ask.dto.AskListDTO;
import com.akatsuki.pioms.ask.service.AskService;
import com.akatsuki.pioms.company.aggregate.CompanyVO;
import com.akatsuki.pioms.company.service.CompanyService;
import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.dashboard.aggregate.ResponseAdminDashBoard;
import com.akatsuki.pioms.dashboard.aggregate.ResponseDriverDashBoard;
import com.akatsuki.pioms.dashboard.aggregate.ResponseFranchiseDashBoard;
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
import com.akatsuki.pioms.frwarehouse.dto.FranchiseWarehouseDTO;
import com.akatsuki.pioms.frwarehouse.service.FranchiseWarehouseService;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.invoice.service.InvoiceService;
import com.akatsuki.pioms.notice.aggregate.NoticeVO;
import com.akatsuki.pioms.notice.service.NoticeService;
import com.akatsuki.pioms.order.aggregate.OrderStat;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final ProductService productService;

    private final FranchiseWarehouseService franchiseWarehouseService;

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
    public DashboardServiceImpl(GetUserInfo getUserInfo, FranchiseService franchiseService,
                                AdminInfoService adminInfoService, OrderService orderService,
                                ExchangeService exchangeService, CompanyService companyService,
                                NoticeService noticeService, ProductService productService,
                                AskService askService, FranchiseOwnerService franchiseOwnerService,
                                InvoiceService invoiceService, FranchiseWarehouseService franchiseWarehouseService) {
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
        this.franchiseWarehouseService = franchiseWarehouseService;
        this.productService = productService;
    }


//    @Override
//    public ResponseAdminDashBoard getRootDash(){
//
//        int rootCode = getUserInfo.getAdminCode();
//        CompanyVO company = companyService.findInformation();
//        OrderStat orderStat = orderService.getOrderStat(rootCode);
//        List<FranchiseDTO> franchises = franchiseService.findFranchiseList();
//        List<ExchangeDTO> exchanges = exchangeService.getExchangesByAdminCode();
//        List<NoticeVO> notices = noticeService.getAllNoticeList();
//        AskListDTO askList = askService.getAllAskList();
//        List<AskDTO> asks = new ArrayList<>(askList.getAsks());
//
//        List<ProductDTO> products = productService.findNotEnoughProducts();
//        return new ResponseAdminDashBoard(company,orderStat,franchises,exchanges,notices,asks,products);
//    }

    @Override
    public ResponseAdminDashBoard getAdminDash(){

        int adminCode = getUserInfo.getAdminCode();
        List<OrderDTO> orders = orderService.getOrderListByAdminCode(adminCode);
        if(adminCode==1){
            CompanyVO company = companyService.findInformation();
            OrderStat orderStat = new OrderStat(orders);
            List<FranchiseDTO> franchises = franchiseService.findFranchiseList();
            List<ExchangeDTO> exchanges = exchangeService.getExchangesByAdminCode();
            List<NoticeVO> notices = noticeService.getAllNoticeList();
            AskListDTO askList = askService.getAllAskList();
            List<AskDTO> asks = new ArrayList<>(askList.getAsks());
            List<ProductDTO> products = productService.findNotEnoughProducts();
            return new ResponseAdminDashBoard(company,orderStat,franchises,exchanges,notices,asks,products);
        }

        CompanyVO company = companyService.findInformation();
        OrderStat orderStat = new OrderStat(orders);
        List<FranchiseDTO> franchises = franchiseService.findFranchiseByAdminCode();
        List<ExchangeDTO> exchanges = exchangeService.getExchangesByAdminCode();
        List<NoticeVO> notices = noticeService.getAllNoticeList();
        AskListDTO askList = askService.getAllAskList();
        List<AskDTO> asks = new ArrayList<>(askList.getAsks());
        List<ProductDTO> products = productService.findNotEnoughProducts();

        return new ResponseAdminDashBoard(company,orderStat,franchises,exchanges,notices,asks,products);
    }

    @Override
    @Transactional
    public ResponseFranchiseDashBoard getFranchiseDash() {
        int franchiseOwnerCode = getUserInfo.getFranchiseOwnerCode();
        List<OrderDTO> orders = orderService.getOrderListByFranchiseOwnerCode(franchiseOwnerCode);
        OrderStat orderStat = new OrderStat(orders);
        List<NoticeVO> notices = noticeService.getAllNoticeList();
        AskListDTO asks = askService.getAllAskList();
        List<FranchiseWarehouseDTO> favorites = franchiseWarehouseService.findFavoritesByOwner(franchiseOwnerCode);

        System.out.println("franchiseOwnerCode = " + franchiseOwnerCode);
        System.out.println("orders = " + orders);
        System.out.println("notices = " + notices);
        System.out.println("asks = " + asks);
        System.out.println("favorites = " + favorites);
        return new ResponseFranchiseDashBoard(orderStat,notices,asks,favorites);
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
