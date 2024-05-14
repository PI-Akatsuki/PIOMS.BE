package com.akatsuki.pioms.specs.service;

import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.order.aggregate.Order;
import com.akatsuki.pioms.order.dto.OrderDTO;
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

    public void postSpecs(int orderCode, int franchiseCode, DELIVERY_DATE deliveryDate){

        specsRepository.save(new SpecsEntity(orderCode, franchiseCode));
        System.out.println("orderDTO = " + orderCode);
    }

    // order 승인 후
    @Override
    public void afterAcceptOrder(int orderCode, int franchiseCode, DELIVERY_DATE deliveryDate){
        System.out.println("명세서 생성 event 발생");
        postSpecs(orderCode, franchiseCode, deliveryDate);
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
        SpecsDTO specsDTO = new SpecsDTO(specsEntity);
        return specsDTO;
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
