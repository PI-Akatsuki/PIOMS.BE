package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.franchise.etc.DELIVERY_DATE;
import com.akatsuki.pioms.invoice.entity.InvoiceEntity;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.invoice.vo.ResponseInvoice;
import com.akatsuki.pioms.invoice.vo.ResponseInvoiceList;
import com.akatsuki.pioms.order.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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

    public void postInvoice(OrderEntity order){
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setOrder(order);
        invoice.setDeliveryStatus(DELIVERY_STATUS.배송전);
        invoice.setInvoiceRegionCode(1);
        DELIVERY_DATE deliveryDate = order.getFranchise().getFranchiseDeliveryDate();
        invoice.setInvoiceDate(setDeliveryTime(order.getOrderDate(),deliveryDate));
        System.out.println("invoice = " + invoice);
        invoiceRepository.save(invoice);
        System.out.println("invoice fin");
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
//        System.out.println("deliveryDay1 = " + deliveryDay1);
//        System.out.println("deliveryDay2 = " + deliveryDay2);

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

    @EventListener
    @Async
    public void getOrder(OrderEvent orderEvent){
        System.out.println("Invoice event listen");
        postInvoice(orderEvent.getOrder());
    }

    public ResponseInvoiceList getAllInvoiceList(){
        List<InvoiceEntity> invoiceList = invoiceRepository.findAll();
        List<ResponseInvoice> responseInvoice = new ArrayList<>();

        invoiceList.forEach(invoiceEntity -> {
            responseInvoice.add(new ResponseInvoice(invoiceEntity));
        });
        return new ResponseInvoiceList(responseInvoice);
    }

    @Override
    public ResponseInvoice putInvoice(int invoiceCode, String invoiceStatus) {
        InvoiceEntity invoiceEntity = invoiceRepository.findById(invoiceCode).orElseThrow(IllegalArgumentException::new);
        invoiceEntity.setDeliveryStatus(DELIVERY_STATUS.valueOf(invoiceStatus));
        invoiceRepository.save(invoiceEntity);
        return new ResponseInvoice(invoiceEntity);
    }

    @Override
    public ResponseInvoice getInvoice(int invoiceCode) {
        InvoiceEntity invoice = invoiceRepository.findById(invoiceCode).orElseThrow(IllegalArgumentException::new);
        return new ResponseInvoice(invoice);
    }

}
