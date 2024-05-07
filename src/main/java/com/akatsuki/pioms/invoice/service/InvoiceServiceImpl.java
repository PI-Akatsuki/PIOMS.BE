package com.akatsuki.pioms.invoice.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.invoice.repository.InvoiceRepository;
import com.akatsuki.pioms.order.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public void postInvoice(OrderEntity order){

    }

    @EventListener
    @Async
    public void getOrder(OrderEvent orderEvent){
        System.out.println("Invoice event listen");
        postInvoice(orderEvent.getOrder());
    }

}
