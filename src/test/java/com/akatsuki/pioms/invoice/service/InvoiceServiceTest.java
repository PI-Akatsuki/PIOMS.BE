package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.admin.aggregate.Admin;
import com.akatsuki.pioms.config.GetUserInfo;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.exchange.service.ExchangeService;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.dto.FranchiseDTO;
import com.akatsuki.pioms.franchise.service.FranchiseService;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.aggregate.ResponseDriverInvoice;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.order.etc.ORDER_CONDITION;
import com.akatsuki.pioms.order.service.OrderService;
import com.akatsuki.pioms.product.aggregate.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private FranchiseService franchiseService;

    @Mock
    private OrderService orderService;

    @Mock
    private ExchangeService exchangeService;

    @Mock
    private GetUserInfo getUserInfo;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Invoice invoice;
    private Order order;
    private Admin admin;
    private Franchise franchise;
    private Product product;
    private FranchiseOwner franchiseOwner;
    private DeliveryDriver deliveryDriver;
    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .adminCode(1)
                .adminId("root")
                .accessNumber("rootAccess")
                .adminStatus(true)
                .adminName("root")
                .adminEmail("root@example.com")
                .adminPhone("010-1234-5678")
                .enrollDate("2023-01-01 00:00:00")
                .updateDate("2023-01-01 00:00:00")
                .adminRole("ROLE_ROOT")
                .franchise(new ArrayList<>())
                .build();

        franchiseOwner = FranchiseOwner.builder()
                .franchiseOwnerCode(1)
                .franchiseOwnerName("test")
                .franchiseOwnerId("sadf")
                .franchiseRole("ROLE_OWNER")
                .build();

        deliveryDriver = DeliveryDriver.builder()
                .driverCode(1)
                .driverId("driver")
                .driverName("driver")
                .driverRole("ROLE_DRIVER").build();

        franchise = Franchise.builder()
                .franchiseCode(1)
                .franchiseOwner(franchiseOwner)
                .admin(admin)
                .franchiseName("franchise")
                .franchiseDeliveryDate(DELIVERY_DATE.월_목)
                .deliveryDriver(deliveryDriver)
                .build();

        order = Order.builder()
                .orderCode(1)
                .orderCondition(ORDER_CONDITION.승인대기)
                .franchise(franchise)
                .orderProductList(null)
                .orderDate(LocalDateTime.now())
                .build();

        invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setDeliveryStatus(DELIVERY_STATUS.배송전);
    }

    @Test
    void testAfterAcceptOrder() {
        //given
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        //when
        invoiceService.afterAcceptOrder(new OrderDTO(order));
        //then
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void testGetAdminInvoiceList_RootAdmin() {
        //given
        when(getUserInfo.getAdminCode()).thenReturn(1);
        when(invoiceRepository.findAllByOrderDesc()).thenReturn(List.of(invoice));
        //when
        List<InvoiceDTO> result = invoiceService.getAdminInvoiceList();
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepository, times(1)).findAllByOrderDesc();
    }

    @Test
    void testGetAdminInvoiceList_NonRootAdmin() {
        //given
        when(getUserInfo.getAdminCode()).thenReturn(2);
        when(invoiceRepository.findAllByOrderFranchiseAdminAdminCodeOrderByInvoiceDateDesc(2))
                .thenReturn(List.of(invoice));
        //when
        List<InvoiceDTO> result = invoiceService.getAdminInvoiceList();
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(invoiceRepository, times(1))
                .findAllByOrderFranchiseAdminAdminCodeOrderByInvoiceDateDesc(2);
    }

    @Test
    void testGetInvoiceByAdminCode() {
        //given
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(invoice));
        //when
        InvoiceDTO result = invoiceService.getInvoiceByAdminCode(1, 1);
        //then
        assertNotNull(result);
    }

    @Test
    void testGetFranchiseInvoiceList() {
        //given
        when(invoiceRepository.findAllByOrderFranchiseFranchiseOwnerFranchiseOwnerCode(1))
                .thenReturn(List.of(invoice));
        //when
        List<InvoiceDTO> result = invoiceService.getFranchiseInvoiceList(1);
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetInvoiceByFranchiseOwnerCode() {
        //given
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(invoice));
        //when
        InvoiceDTO result = invoiceService.getInvoiceByFranchiseOwnerCode(1, 1);
        //then
        assertNotNull(result);
    }

    @Test
    void testPutInvoice() {
        //given
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        //when
        InvoiceDTO result = invoiceService.putInvoice(1, 1, DELIVERY_STATUS.배송중);
        //then
        assertNotNull(result);
        assertEquals(DELIVERY_STATUS.배송중, result.getDeliveryStatus());
    }

    @Test
    void testCheckInvoiceStatus() {
        //given
        invoice.setDeliveryStatus(DELIVERY_STATUS.배송완료);
        when(invoiceRepository.findByOrderOrderCode(1)).thenReturn(invoice);
        //when
        Boolean result = invoiceService.checkInvoiceStatus(1);
        //then
        assertTrue(result);
    }

    @Test
    void testGetInvoiceByOrderCode() {
        //given
        when(invoiceRepository.findByOrderOrderCode(1)).thenReturn(invoice);
        //when
        InvoiceDTO result = invoiceService.getInvoiceByOrderCode(1);
        //then
        assertNotNull(result);
    }

    @Test
    void testDeleteInvoice() {
        //given
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(invoice));
        //when
        boolean result = invoiceService.deleteInvoice(1, 1);
        //then
        assertTrue(result);
        verify(invoiceRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetAllDriverInvoiceList() {
        //given
        when(franchiseService.findFranchiseListByDriverCode(1))
                .thenReturn(List.of(new FranchiseDTO()));
        when(invoiceRepository.findByOrderFranchiseFranchiseCode(anyInt()))
                .thenReturn(List.of(invoice));
        //when
        List<ResponseDriverInvoice> result = invoiceService.getAllDriverInvoiceList(1);
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetStatusBeforeDeliveryDriverInvoiceList() {
        //given
        when(franchiseService.findFranchiseListByDriverCode(1))
                .thenReturn(List.of(new FranchiseDTO()));
        when(invoiceRepository.findByOrderFranchiseFranchiseCode(anyInt()))
                .thenReturn(List.of(invoice));
        //when
        List<ResponseDriverInvoice> result = invoiceService.getStatusBeforeDeliveryDriverInvoiceList(1);
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetStatusIngDeliveryDriverInvoiceList() {
        //given
        invoice.setDeliveryStatus(DELIVERY_STATUS.배송중);
        when(franchiseService.findFranchiseListByDriverCode(1))
                .thenReturn(List.of(new FranchiseDTO()));
        when(invoiceRepository.findByOrderFranchiseFranchiseCode(anyInt()))
                .thenReturn(List.of(invoice));
        //when
        List<ResponseDriverInvoice> result = invoiceService.getStatusIngDeliveryDriverInvoiceList(1);
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetStatusCompleteDeliveryDriverInvoiceList() {
        //given
        invoice.setDeliveryStatus(DELIVERY_STATUS.배송완료);
        when(franchiseService.findFranchiseListByDriverCode(1))
                .thenReturn(List.of(new FranchiseDTO()));
        when(invoiceRepository.findByOrderFranchiseFranchiseCode(anyInt()))
                .thenReturn(List.of(invoice));
        //when
        List<ResponseDriverInvoice> result = invoiceService.getStatusCompleteDeliveryDriverInvoiceList(1);
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testModifyInvoiceStatusByDriver() {
        //given
        when(invoiceRepository.findById(1)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        //when
        boolean result = invoiceService.modifyInvoiceStatusByDriver(1, 1, DELIVERY_STATUS.배송중);
        //then
        assertTrue(result);
        assertEquals(DELIVERY_STATUS.배송중, invoice.getDeliveryStatus());
    }

    @Test
    void testCountStatusBeforeDeliveryDriverInvoices() {
        //given
        when(franchiseService.findFranchiseListByDriverCode(1))
                .thenReturn(List.of(new FranchiseDTO()));
        when(invoiceRepository.findByOrderFranchiseFranchiseCode(anyInt()))
                .thenReturn(List.of(invoice));
        //when
        int result = invoiceService.countStatusBeforeDeliveryDriverInvoices(1);
        //then
        assertEquals(1, result);
    }

    @Test
    void testCountStatusIngDeliveryDriverInvoices() {
        //given
        invoice.setDeliveryStatus(DELIVERY_STATUS.배송중);
        when(franchiseService.findFranchiseListByDriverCode(1))
                .thenReturn(List.of(new FranchiseDTO()));
        when(invoiceRepository.findByOrderFranchiseFranchiseCode(anyInt()))
                .thenReturn(List.of(invoice));
        //when
        int result = invoiceService.countStatusIngDeliveryDriverInvoices(1);
        //then
        assertEquals(1, result);
    }

    @Test
    void testCountStatusCompleteDeliveryDriverInvoices() {
        //given
        invoice.setDeliveryStatus(DELIVERY_STATUS.배송완료);
        when(franchiseService.findFranchiseListByDriverCode(1))
                .thenReturn(List.of(new FranchiseDTO()));
        when(invoiceRepository.findByOrderFranchiseFranchiseCode(anyInt()))
                .thenReturn(List.of(invoice));
        //when
        int result = invoiceService.countStatusCompleteDeliveryDriverInvoices(1);
        //then
        assertEquals(1, result);
    }
}
