package com.akatsuki.pioms.specs.service;

import com.akatsuki.pioms.event.OrderEvent;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.specs.aggregate.SpecsEntity;
import com.akatsuki.pioms.specs.dto.SpecsDTO;
import com.akatsuki.pioms.specs.repository.SpecsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecsServiceImpl implements SpecsService{
    private final SpecsRepository specsRepository;

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
    public List<SpecsDTO> getSpecsList(){
        List<SpecsEntity> specsList = specsRepository.findAll();
        List<SpecsDTO> responseSpecs = new ArrayList<>();
        specsList.forEach(specs -> {
            responseSpecs.add(new SpecsDTO(specs));
        });
        System.out.println(responseSpecs.size());
        return responseSpecs;
    }

    @Override
    public SpecsDTO getSpecs(int specsCode){
        SpecsEntity specsEntity = specsRepository.findById(specsCode).orElseThrow(IllegalArgumentException::new);
        return new SpecsDTO(specsEntity);
    }

    @Override
    public List<SpecsDTO> getFranchiseSpecsList(int franchiseCode) {
        List<SpecsEntity> specsEntities = specsRepository.findAllByFranchiseFranchiseCode(franchiseCode);
        List<SpecsDTO> responseSpecs = new ArrayList<>();
        specsEntities.forEach(specs -> {
             responseSpecs.add(new SpecsDTO(specs));
        });
        return responseSpecs;
    }

}
