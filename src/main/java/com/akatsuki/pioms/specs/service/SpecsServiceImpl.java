package com.akatsuki.pioms.specs.service;

import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.specs.aggregate.SpecsEntity;
import com.akatsuki.pioms.specs.dto.SpecsDTO;
import com.akatsuki.pioms.specs.repository.SpecsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SpecsDTO getSpecsByFranchiseCode(int franchiseCode, int specsId) {
        SpecsEntity specs = specsRepository.findById(specsId).orElse(null);
        if (specs == null || specs.getOrder().getFranchise().getFranchiseCode() != franchiseCode){
            return null;
        }

        return new SpecsDTO(specs);
    }

    @Override
    public List<SpecsDTO> getSpecsListByAdminCode(int adminCode) {
        if (adminCode ==1 ){
            return getSpecsList();
        }
        List<SpecsEntity> specsList = specsRepository.findAllByOrderFranchiseAdminAdminCode(adminCode);
        List<SpecsDTO> specsDTOS = new ArrayList<>();
        for (int i = 0; i < specsList.size(); i++) {
            specsDTOS.add(new SpecsDTO(specsList.get(i)));
        }
        return specsDTOS;
    }

    @Override
    public SpecsDTO getSpecsByAdminCode(int adminCode, int specsCode) {
        SpecsEntity specs = specsRepository.findById(specsCode).orElse(null);
        if (adminCode==1 && specs!=null)
            return new SpecsDTO(specs);
        if (specs==null || specs.getOrder().getFranchise().getAdmin().getAdminCode() != adminCode){
            return null;
        }
        return new SpecsDTO(specs);
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
        List<SpecsEntity> specsEntities = specsRepository.findAllByOrderFranchiseFranchiseCode(franchiseCode);
        List<SpecsDTO> responseSpecs = new ArrayList<>();
        specsEntities.forEach(specs -> {
             responseSpecs.add(new SpecsDTO(specs));
        });

        return responseSpecs;
    }

}
