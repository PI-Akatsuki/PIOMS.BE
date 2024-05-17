package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.driver.dto.DeliveryRegionDTO;
import com.akatsuki.pioms.driver.aggregate.DeliveryDriver;
import com.akatsuki.pioms.driver.aggregate.DeliveryRegion;
import com.akatsuki.pioms.driver.service.DeliveryService;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

@Service
@Log4j2
public class InvoiceServiceImpl implements InvoiceService {
    final private InvoiceRepository invoiceRepository;
    final private DeliveryService deliveryService;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,DeliveryService deliveryService) {
        this.invoiceRepository = invoiceRepository;
        this.deliveryService = deliveryService;
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
    public void afterAcceptOrder(OrderDTO orderDTO)
    {
        Order order = new Order(orderDTO);
        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setDeliveryStatus(DELIVERY_STATUS.배송전);

        int deliveryRegionCode = deliveryService.getDeliveryRegionCodeByFranchiseCode(orderDTO.getFranchiseCode());
        invoice.setDeliveryRegion(deliveryRegionCode);
        invoice.setInvoiceDate(setDeliveryTime(order.getOrderDate(), orderDTO.getDeliveryDate()));
        invoiceRepository.save(invoice);
    }


    @Override
    public List<InvoiceDTO> getAdminInvoiceList(int adminCode) {
        List<Invoice> invoices;

        if (adminCode==1)
            invoices = invoiceRepository.findAll();
        else
            invoices = invoiceRepository.findAllByOrderFranchiseAdminAdminCode(adminCode);

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

    public List<InvoiceDTO> getAllInvoiceList(){
        List<Invoice> invoiceList = invoiceRepository.findAll();
        List<InvoiceDTO> responseInvoice = new ArrayList<>();

        invoiceList.forEach(invoiceEntity -> {
            responseInvoice.add(new InvoiceDTO(invoiceEntity));
        });
        return responseInvoice;
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
        invoiceRepository.save(invoice);

        return new InvoiceDTO(invoice);
    }


    public Boolean checkInvoiceStatus(int orderCode){
        Invoice invoice = invoiceRepository.findByOrderOrderCode(orderCode);
        if (invoice.getDeliveryStatus() == DELIVERY_STATUS.배송완료){
            System.out.println("invoice = " + invoice.getDeliveryStatus());
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

}
