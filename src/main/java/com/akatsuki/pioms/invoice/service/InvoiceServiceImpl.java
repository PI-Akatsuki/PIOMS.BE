package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.invoice.aggregate.Invoice;
import com.akatsuki.pioms.invoice.dto.InvoiceDTO;
import com.akatsuki.pioms.invoice.aggregate.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.aggregate.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    final private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public InvoiceDTO postInvoice(int orderCode, int franchiseCode, DELIVERY_DATE franchiseDeliveryDate, LocalDateTime orderDateTime){
        Invoice invoice = new Invoice();
        Order order = new Order();
        order.setOrderCode(orderCode);
        invoice.setOrder(order);

        invoice.setDeliveryStatus(DELIVERY_STATUS.배송전);

//        invoice.setDeliveryRegion(1);

        DELIVERY_DATE deliveryDate = franchiseDeliveryDate;
        invoice.setInvoiceDate(setDeliveryTime(orderDateTime, deliveryDate));


        return saveInvoice(invoice);

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
    public void afterAcceptOrder(int orderCode, int franchiseCode, DELIVERY_DATE deliveryDate, LocalDateTime orderDateTime){
        System.out.println("Invoice event listen");
        postInvoice(orderCode,franchiseCode, deliveryDate, orderDateTime);
        System.out.println("Invoice event End");
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
    public InvoiceDTO putInvoice(int invoiceCode, DELIVERY_STATUS invoiceStatus) {
        System.out.println("invoiceStatus = " + invoiceStatus);
        Invoice invoiceEntity = invoiceRepository.findById(invoiceCode).orElseThrow(IllegalArgumentException::new);

        invoiceEntity.setDeliveryStatus(invoiceStatus);
        invoiceRepository.save(invoiceEntity);
        return new InvoiceDTO(invoiceEntity);
    }

    @Override
    public InvoiceDTO getInvoice(int invoiceCode) {
        Invoice invoice = invoiceRepository.findById(invoiceCode).orElseThrow(IllegalArgumentException::new);
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
        return new InvoiceDTO(invoiceRepository.findByOrderOrderCode(orderCode));
    }

    @Override
    public InvoiceDTO saveInvoice(Invoice invoice) {

        return new InvoiceDTO(invoiceRepository.save(invoice));
    }

    @Override
    public void deleteInvoice(Invoice invoiceDTO) {
        invoiceRepository.delete(invoiceDTO);
    }

}
