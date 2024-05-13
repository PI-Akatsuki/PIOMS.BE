package com.akatsuki.pioms.specs.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.order.entity.Order;
import com.akatsuki.pioms.specs.aggregate.ResponseSpecs;
import com.akatsuki.pioms.specs.aggregate.SpecsEntity;
import com.akatsuki.pioms.specs.repository.SpecsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecsServiceImpl implements SpecsService{
    private SpecsRepository specsRepository;

    @Autowired
    public SpecsServiceImpl(SpecsRepository specsRepository) {
        this.specsRepository = specsRepository;
    }

    public void postSpecs(Order orderEntity){
        specsRepository.save(new SpecsEntity(orderEntity));
        System.out.println("orderEntity = " + orderEntity);
    }

    @EventListener
    @Async
    public void getOrder(OrderEvent orderEvent){
        System.out.println("명세서 생성 event 발생");
        postSpecs(orderEvent.getOrder());
        System.out.println("명세서 생성 event 완료");
    }

    @Override
    public List<ResponseSpecs> getSpecsList(){
        List<SpecsEntity> specsList = specsRepository.findAll();
        List<ResponseSpecs> responseSpecs = new ArrayList<>();
        specsList.forEach(specs -> {
            responseSpecs.add(new ResponseSpecs(specs));
        });
        System.out.println(responseSpecs.size());
        return responseSpecs;
    }

    @Override
    public ResponseSpecs getSpecs(int specsCode){
        SpecsEntity specsEntity = specsRepository.findById(specsCode).orElseThrow(IllegalArgumentException::new);
        return new ResponseSpecs(specsEntity);
    }

    @Override
    public List<ResponseSpecs> getFranchiseSpecsList(int franchiseCode) {
        List<SpecsEntity> specsEntities = specsRepository.findAllByFranchiseFranchiseCode(franchiseCode);
        List<ResponseSpecs> responseSpecs = new ArrayList<>();
        specsEntities.forEach(specs -> {
             responseSpecs.add(new ResponseSpecs(specs));
        });
        return responseSpecs;
    }

}
