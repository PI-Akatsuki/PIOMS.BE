package com.akatsuki.pioms.invoice.service;



import com.akatsuki.pioms.company.repository.CompanyRepository;
import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.exchange.dto.ExchangeDTO;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.repository.FranchiseRepository;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frowner.service.FranchiseOwnerService;
import com.akatsuki.pioms.invoice.aggregate.*;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.aggregate.OrderProduct;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.repository.OrderRepository;
import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.product.aggregate.Product;
import com.akatsuki.pioms.product.dto.ProductDTO;
import com.akatsuki.pioms.product.etc.PRODUCT_COLOR;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class InvoiceServiceImpl implements InvoiceService {

    final private InvoiceRepository invoiceRepository;
    final private FranchiseService franchiseService;
    final private OrderService orderService;
    final private ExchangeService exchangeService;

    private final GetUserInfo getUserInfo;
    private final CompanyRepository companyRepository;
    private final FranchiseRepository franchiseRepository;
    private final OrderRepository orderRepository;
    private final FranchiseOwnerService franchiseOwnerService;


    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, FranchiseService franchiseService, FranchiseRepository franchiseRepository1,
                              OrderService orderService, ExchangeService exchangeService, CompanyRepository companyRepository, OrderRepository orderRepository
                              ,GetUserInfo getUserInfo, FranchiseOwnerService franchiseOwnerService) {

        this.invoiceRepository = invoiceRepository;
        this.franchiseService = franchiseService;
        this.orderService = orderService;
        this.exchangeService = exchangeService;
        this.getUserInfo = getUserInfo;
        this.companyRepository = companyRepository;
        this.franchiseRepository = franchiseRepository1;
        this.orderRepository = orderRepository;
        this.franchiseOwnerService = franchiseOwnerService;
    }

    public LocalDateTime setDeliveryTime(LocalDateTime orderTime, DELIVERY_DATE deliveryDate){

        int day = orderTime.getDayOfWeek().getValue();
        int deliveryDay1, deliveryDay2;
        if(deliveryDate == DELIVERY_DATE.월_목){
            deliveryDay1 = 1;
            deliveryDay2 = 4;
        }else if(deliveryDate == DELIVERY_DATE.화_금){
            deliveryDay1 = 2;
            deliveryDay2 = 5;
        }else {
            deliveryDay1 = 3;
            deliveryDay2 = 5;
        }

        int changeDay=0;
        if(day < deliveryDay1 || day>=deliveryDay2){

            if(day>=deliveryDay2){
                changeDay = 7-day + deliveryDay1;
            }else {
                changeDay = Math.abs(deliveryDay1-day);
            }
        }else {
            changeDay = deliveryDay2 - day;
        }
        orderTime= orderTime.plusDays(changeDay);
        System.out.println("orderTime = " + orderTime + " 요일: "+ orderTime.getDayOfWeek());
        return orderTime;
    }

    @Override
    @Transactional
    public void afterAcceptOrder(OrderDTO orderDTO)
    {
        Order order = new Order(orderDTO);
        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setDeliveryStatus(DELIVERY_STATUS.배송전);
        invoice.setInvoiceDate(setDeliveryTime(order.getOrderDate(), orderDTO.getDeliveryDate()));
        invoiceRepository.save(invoice);
    }


    @Override
    public List<InvoiceDTO> getAdminInvoiceList() {
        int adminCode = getUserInfo.getAdminCode();
        List<Invoice> invoices;

        if (adminCode==1)
            invoices = invoiceRepository.findAllByOrderDesc();
        else
            invoices = invoiceRepository.findAllByOrderFranchiseAdminAdminCodeOrderByInvoiceDateDesc(adminCode);

        List<InvoiceDTO> invoiceDTOS= new ArrayList<>();
        for (int i = 0; i < invoices.size(); i++) {
            invoiceDTOS.add(new InvoiceDTO(invoices.get(i)));
        }
        return invoiceDTOS;
    }

    @Override
    public InvoiceDTO getInvoiceByAdminCode(int adminCode, int invoiceCode) {
        Invoice invoice = invoiceRepository.findById(invoiceCode).orElse(null);

        if (adminCode==1 || invoice!=null && invoice.getOrder().getFranchise().getAdmin().getAdminCode() == adminCode){
            return new InvoiceDTO(invoice);
        }

        return null;
    }

    @Override
    public List<InvoiceDTO> getFranchiseInvoiceList(int franchiseOwnerCode) {
        List<Invoice> invoices = invoiceRepository.findAllByOrderFranchiseFranchiseOwnerFranchiseOwnerCode(franchiseOwnerCode);
        if (invoices == null)
            return null;
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        for (int i = 0; i < invoices.size(); i++) {
            invoiceDTOS.add(new InvoiceDTO(invoices.get(i)));
        }
        return invoiceDTOS;
    }

    @Override
    public InvoiceDTO getInvoiceByFranchiseOwnerCode(int franchiseOwnerCode, int invoiceCode) {
        Invoice invoice = invoiceRepository.findById(invoiceCode).orElse(null);

        if (invoice==null || invoice.getOrder().getFranchise().getFranchiseOwner().getFranchiseOwnerCode() != franchiseOwnerCode)
            return null;

        return new InvoiceDTO(invoice);
    }

    @Override
    public InvoiceDTO putInvoice(int adminCode, int invoiceCode, DELIVERY_STATUS invoiceStatus) {
        Invoice invoice = invoiceRepository.findById(invoiceCode).orElse(null);
        if (invoice== null)
            return  null;
        if ( invoice.getOrder().getFranchise().getAdmin().getAdminCode() != adminCode && adminCode!=1){
            return null;
        }
        invoice.setDeliveryStatus(invoiceStatus);
        invoice = invoiceRepository.save(invoice);

        if (invoiceStatus == DELIVERY_STATUS.배송중 ){
            // 반환 대기중인 반품품목 추가하기
            exchangeService.updateExchangeStartDelivery(invoice.getOrder().getFranchise().getFranchiseCode());
        }

        if (invoiceStatus == DELIVERY_STATUS.배송완료){
            orderService.putOrderCondition(invoice.getOrder().getOrderCode(), ORDER_CONDITION.검수대기);
            if (invoice.getOrder().getExchange() !=null)
                exchangeService.updateExchangeToCompany(invoice.getOrder().getExchange().getExchangeCode());
            exchangeService.updateExchangeEndDelivery(invoice.getOrder().getFranchise().getFranchiseCode());
        }
        return new InvoiceDTO(invoice);
    }


    public Boolean checkInvoiceStatus(int orderCode){
        Invoice invoice = invoiceRepository.findByOrderOrderCode(orderCode);
        if (invoice.getDeliveryStatus() == DELIVERY_STATUS.배송완료){
            return true;
        }
        return false;
    }
    public InvoiceDTO getInvoiceByOrderCode(int orderCode){
        Invoice invoice =  invoiceRepository.findByOrderOrderCode(orderCode);
        if (invoice== null)
            return null;

        return new InvoiceDTO(invoice);
    }

    @Override
    public boolean deleteInvoice(int franchiseOwnerCode,int invoiceCode) {
        Invoice invoice = invoiceRepository.findById(invoiceCode).orElse(null);

        if (invoice==null || invoice.getOrder().getFranchise().getFranchiseOwner().getFranchiseOwnerCode() != franchiseOwnerCode
        || invoice.getDeliveryStatus()!= DELIVERY_STATUS.배송전 )
            return false;

        invoiceRepository.deleteById(invoiceCode);

        return true;
    }

    // 배송상태조회 - 배송기사코드로 담당지역의 배송상태 전체조회
    @Override
    @Transactional(readOnly = true)
    public List<ResponseDriverInvoice> getAllDriverInvoiceList(int driverCode) {

        // 배송기사가 담당하고 있는 지역에 배송목록이 있는지 여부 확인
        List<FranchiseDTO> franchise = franchiseService.findFranchiseListByDriverCode(driverCode);
        // 배송기사 송장 목록
        List<Invoice> driverInvoiceList = new ArrayList<>();

        // 배송기사 코드로 송장 목록을 가져와 그 갯수만큼 추가
        for (int i = 0; i < franchise.size(); i++) {
            List<Invoice> invoices = invoiceRepository.findByOrderFranchiseFranchiseCode(franchise.get(i).getFranchiseCode());
            if (invoices != null && !invoices.isEmpty())
                driverInvoiceList.addAll(invoices);
        }

        // 배송기사의 배송(송장) 리스트 조회
        List<ResponseDriverInvoice> responseDriverInvoices = new ArrayList<>();
        driverInvoiceList.forEach(
                // entity를 DTO로 변환
                invoice -> {
                    InvoiceDTO invoiceDTO = new InvoiceDTO(invoice);
                    responseDriverInvoices.add(new ResponseDriverInvoice(driverCode, invoiceDTO));
                }
        );

        return responseDriverInvoices;
    }

    // 배송상태조회 - 배송기사코드와 담당지역의 배송상태에 따른 상세조회 (배송전)
    @Override
    @Transactional(readOnly = true)
    public List<ResponseDriverInvoice> getStatusBeforeDeliveryDriverInvoiceList(int driverCode) {

        List<ResponseDriverInvoice> responseDriverInvoices = getAllDriverInvoiceList(driverCode);
        List<ResponseDriverInvoice> returnList = new ArrayList<>();
        for (int i = 0; i < responseDriverInvoices.size(); i++) {
            ResponseDriverInvoice responseDriverInvoice = responseDriverInvoices.get(i);
            if (responseDriverInvoice.getDeliveryStatus() == DELIVERY_STATUS.배송전)
                returnList.add(responseDriverInvoice);
        }

        return returnList;
    }

    // 배송상태조회 - 배송기사코드와 담당지역의 배송상태에 따른 상세조회 (배송중)
    @Override
    @Transactional(readOnly = true)
    public List<ResponseDriverInvoice> getStatusIngDeliveryDriverInvoiceList(int driverCode) {

        List<ResponseDriverInvoice> responseDriverInvoices = getAllDriverInvoiceList(driverCode);
        List<ResponseDriverInvoice> returnList = new ArrayList<>();
        for (int i = 0; i < responseDriverInvoices.size(); i++) {
            ResponseDriverInvoice responseDriverInvoice = responseDriverInvoices.get(i);
            if (responseDriverInvoice.getDeliveryStatus() == DELIVERY_STATUS.배송중)
                returnList.add(responseDriverInvoice);
        }

        return returnList;
    }

    // 배송상태조회 - 배송기사코드와 담당지역의 배송상태에 따른 상세조회 (배송완료)
    @Override
    @Transactional(readOnly = true)
    public List<ResponseDriverInvoice> getStatusCompleteDeliveryDriverInvoiceList(int driverCode) {

        List<ResponseDriverInvoice> responseDriverInvoices = getAllDriverInvoiceList(driverCode);
        List<ResponseDriverInvoice> returnList = new ArrayList<>();
        for (int i = 0; i < responseDriverInvoices.size(); i++) {
            ResponseDriverInvoice responseDriverInvoice = responseDriverInvoices.get(i);
            if (responseDriverInvoice.getDeliveryStatus() == DELIVERY_STATUS.배송완료)
                returnList.add(responseDriverInvoice);
        }

        return returnList;
    }

    // 배송기사가 상태변경 수정
    @Override
    @Transactional
    public boolean modifyInvoiceStatusByDriver(int invoiceCode, int driverCode, DELIVERY_STATUS deliveryStatus) {

        // 송장이 있는지 여부
        Invoice invoice = invoiceRepository.findById(invoiceCode).orElse(null);
        if (invoice == null) {
            return false;
        }

        // 배송 상태 변경 후 레포에 저장
        invoice.setDeliveryStatus(deliveryStatus);
        invoiceRepository.save(invoice);


        if (deliveryStatus == DELIVERY_STATUS.배송중 ){
            // 반환 대기중인 반품품목 추가하기
            exchangeService.updateExchangeStartDelivery(invoice.getOrder().getFranchise().getFranchiseCode());
            
        }
        // 배송기사 배송완료 시 배송중 -> 배송완료
        if(deliveryStatus == DELIVERY_STATUS.배송완료) {
            if (invoice.getOrder().getExchange() !=null)
                exchangeService.updateExchangeToCompany(invoice.getOrder().getExchange().getExchangeCode());
            // 점주가 확인 전까지 '검수대기'
            orderService.putOrderCondition(invoice.getOrder().getOrderCode(), ORDER_CONDITION.검수대기);
            exchangeService.updateExchangeEndDelivery(invoice.getOrder().getFranchise().getFranchiseCode());
        }

        return true;
    }

    // 배송상태조회 - 배송전 상태의 송장 수 조회
    @Override
    @Transactional(readOnly = true)
    public int countStatusBeforeDeliveryDriverInvoices(int driverCode) {
        List<ResponseDriverInvoice> responseDriverInvoices = getAllDriverInvoiceList(driverCode);
        int count = 0;
        for (ResponseDriverInvoice responseDriverInvoice : responseDriverInvoices) {
            if (responseDriverInvoice.getDeliveryStatus() == DELIVERY_STATUS.배송전) {
                count++;
            }
        }
        return count;
    }

    // 배송상태조회 - 배송중 상태의 송장 수 조회
    @Override
    @Transactional(readOnly = true)
    public int countStatusIngDeliveryDriverInvoices(int driverCode) {
        List<ResponseDriverInvoice> responseDriverInvoices = getAllDriverInvoiceList(driverCode);
        int count = 0;
        for (ResponseDriverInvoice responseDriverInvoice :
                responseDriverInvoices) {
            if (responseDriverInvoice.getDeliveryStatus() == DELIVERY_STATUS.배송중) {
                count++;
            }
        }
        return count;
    }

    // 배송상태조회 - 배송중 상태의 송장 수 조회
    @Override
    @Transactional(readOnly = true)
    public int countStatusCompleteDeliveryDriverInvoices(int driverCode) {

        List<ResponseDriverInvoice> responseDriverInvoices = getAllDriverInvoiceList(driverCode);
        int count = 0;
        for (ResponseDriverInvoice responseDriverInvoice : responseDriverInvoices) {
            if (responseDriverInvoice.getDeliveryStatus() == DELIVERY_STATUS.배송완료) {
                count++;
            }
        }
        return count;
    }

    @Override
    public List<InvoiceDTO> getInvoicesByDriverCode(int driverCode) {
        List<Invoice> invoices = invoiceRepository.findAllByOrderFranchiseDeliveryDriverDriverCode(driverCode);
        List<InvoiceDTO> invoiceDTOS = new ArrayList<>();
        for (int i = 0; i < invoices.size(); i++) {
            invoiceDTOS.add(new InvoiceDTO(invoices.get(i)));
        }
        return invoiceDTOS;
    }

    // 배송기사 id로 배송 조회 시 송장 상세조회 기능 구현
    @Override
    public ResponseInvoiceDetail getInvoiceDetail(int invoiceCode) {
        int driverCode= getUserInfo.getDriverCode();
        Invoice invoice = invoiceRepository.findByInvoiceCode(invoiceCode);


//        if (invoice != null) {
//            throw new RuntimeException("송장을 찾을 수 없습니다.");
//        }
        Order order = invoice.getOrder();
//        List<OrderProduct> orderProductList = order.getOrderProductList();
        Franchise franchise = order.getFranchise();

        List<Product> product = new ArrayList<>();
        for (OrderProduct orderProduct : order.getOrderProductList()) {
            product.add(orderProduct.getProduct());
        }
        List<ExchangeDTO> exchangeDTOS = exchangeService.findExchangeInDeliveryCompanyToFranchise(franchise.getFranchiseOwner().getFranchiseOwnerCode());

        return new ResponseInvoiceDetail(invoice, order, franchise, product, exchangeDTOS);
    }
}
