package com.akatsuki.pioms.specs.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.order.entity.OrderEntity;
import com.akatsuki.pioms.specs.entity.SpecsEntity;
import com.akatsuki.pioms.specs.repository.SpecsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SpecsServiceImpl implements SpecsService{
    private SpecsRepository specsRepository;

    @Autowired
    public SpecsServiceImpl(SpecsRepository specsRepository) {
        this.specsRepository = specsRepository;
    }

    public void postSpecs(OrderEntity orderEntity){
        specsRepository.save(new SpecsEntity(orderEntity));
        //만약 교환할 것이 있다면?

    }

    @EventListener
    @Async
    public void getOrder(OrderEvent orderEvent){
        System.out.println("명세서 생성 event 발생");
        postSpecs(orderEvent.getOrder());
        System.out.println("명세서 생성 event 완료");
    }


}
