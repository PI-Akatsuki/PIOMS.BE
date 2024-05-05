package com.akatsuki.pioms.specs.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.order.entity.OrderEntity;
import com.akatsuki.pioms.specs.entity.SpecsEntity;
import com.akatsuki.pioms.specs.repository.SpecsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class SpecsServiceImpl implements SpecsService{
    private SpecsRepository specsRepository;

    @Autowired
    public SpecsServiceImpl(SpecsRepository specsRepository) {
        this.specsRepository = specsRepository;
    }

    public void postSpecs(OrderEntity orderEntity){
//        SpecsEntity specs = new SpecsEntity(orderId,franchiseId);
        specsRepository.save(new SpecsEntity(orderEntity));
    }

    @EventListener
    public void getOrder(OrderEvent orderEvent){
        System.out.println("명세서 생성 event 발생");
        postSpecs(orderEvent.getOrder());
        System.out.println("명세서 생성 event 완료");
    }


}
