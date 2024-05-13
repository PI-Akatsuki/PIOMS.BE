package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.invoice.aggregate.InvoiceEntity;
import com.akatsuki.pioms.invoice.etc.DELIVERY_STATUS;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.aggregate.Order;
import org.aspectj.weaver.ast.Or;
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

    @Override
    public InvoiceEntity postInvoice(Order orderDTO){
        InvoiceEntity invoice = new InvoiceEntity();
        if(orderDTO!=null) {
            Order order = new Order();
            order.setOrderCode(orderDTO.getOrderCode());
            invoice.setOrder(order);
            invoice.setDeliveryStatus(DELIVERY_STATUS.배송전);
            invoice.setInvoiceRegionCode(1);
            DELIVERY_DATE deliveryDate = orderDTO.getFranchise().getFranchiseDeliveryDate();
            invoice.setInvoiceDate(setDeliveryTime(orderDTO.getOrderDate(), deliveryDate));
        }

        return saveInvoice( new InvoiceEntity( invoice));

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

    @EventListener
    @Async
    public void getOrder(OrderEvent orderEvent){
        System.out.println("Invoice event listen");
        postInvoice(orderEvent.getOrder());
        System.out.println("Invoice event End");
    }

    public List<InvoiceEntity> getAllInvoiceList(){
        List<InvoiceEntity> invoiceList = invoiceRepository.findAll();
        List<InvoiceEntity> responseInvoice = new ArrayList<>();

        invoiceList.forEach(invoiceEntity -> {
            responseInvoice.add(new InvoiceEntity(invoiceEntity));
        });
        return responseInvoice;
    }

    @Override
    public InvoiceEntity putInvoice(int invoiceCode, DELIVERY_STATUS invoiceStatus) {
        System.out.println("invoiceStatus = " + invoiceStatus);
        InvoiceEntity invoiceEntity = invoiceRepository.findById(invoiceCode).orElseThrow(IllegalArgumentException::new);

        invoiceEntity.setDeliveryStatus(invoiceStatus);
        invoiceRepository.save(invoiceEntity);
        return new InvoiceEntity(invoiceEntity);
    }

    @Override
    public InvoiceEntity getInvoice(int invoiceCode) {
        InvoiceEntity invoice = invoiceRepository.findById(invoiceCode).orElseThrow(IllegalArgumentException::new);
        return new InvoiceEntity(invoice);
    }

    public Boolean checkInvoiceStatus(int orderCode){
        InvoiceEntity invoice = invoiceRepository.findByOrderOrderCode(orderCode);
        if (invoice.getDeliveryStatus() == DELIVERY_STATUS.배송완료){
            System.out.println("invoice = " + invoice.getDeliveryStatus());
            return true;
        }
        return false;
    }
    public InvoiceEntity getInvoiceByOrderCode(int orderCode){
        return new InvoiceEntity(invoiceRepository.findByOrderOrderCode(orderCode));
    }

    @Override
    public InvoiceEntity saveInvoice(InvoiceEntity invoiceDTO) {
        InvoiceEntity invoice = new InvoiceEntity(invoiceDTO);
        return new InvoiceEntity(invoiceRepository.save(invoice));
    }

    @Override
    public void deleteInvoice(InvoiceEntity invoiceDTO) {
        invoiceRepository.delete(new InvoiceEntity(invoiceDTO));
    }

}
