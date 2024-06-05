package com.akatsuki.pioms.specs.service;

import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.order.dto.OrderDTO;
import com.akatsuki.pioms.specs.aggregate.Specs;
import com.akatsuki.pioms.specs.dto.SpecsDTO;
import com.akatsuki.pioms.specs.repository.SpecsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecsServiceImpl implements SpecsService{
    private final SpecsRepository specsRepository;

    @Autowired
    public SpecsServiceImpl(SpecsRepository specsRepository) {
        this.specsRepository = specsRepository;
    }

    // order 승인 후
    @Override
    @Transactional
    public void afterAcceptOrder(OrderDTO orderDTO){
        specsRepository.save(new Specs(orderDTO.getOrderCode(), orderDTO.getFranchiseCode()));
    }

    @Override
    public List<SpecsDTO> getSpecsListByAdminCode(int adminCode) {
        if (adminCode ==1 ){
            // root 인 경우
            return getSpecsList();
        }

        List<Specs> specsList = specsRepository.findAllByOrderFranchiseAdminAdminCode(adminCode);
        if (specsList.isEmpty())
            return null;

        List<SpecsDTO> specsDTOS = new ArrayList<>();

        for (int i = 0; i < specsList.size(); i++) {
            specsDTOS.add(new SpecsDTO(specsList.get(i)));
        }

        return specsDTOS;
    }

    @Override
    public SpecsDTO getSpecsByAdminCode(int adminCode, int specsCode) {
        Specs specs = specsRepository.findById(specsCode).orElse(null);
        if (adminCode==1 && specs!=null)
            return new SpecsDTO(specs);
        if (specs==null || specs.getOrder().getFranchise().getAdmin().getAdminCode() != adminCode)
            return null;
        return new SpecsDTO(specs);
    }

    @Override
    public List<SpecsDTO> getSpecsList(){
        List<Specs> specsList = specsRepository.findAll();
        List<SpecsDTO> responseSpecs = new ArrayList<>();

        specsList.forEach(specs -> {
            responseSpecs.add(new SpecsDTO(specs));
        });
        System.out.println(responseSpecs.size());
        return responseSpecs;
    }

    @Override
    public List<SpecsDTO> getSpecsListByFrOwnerCode(int franchiseCode) {
        List<Specs> specsEntities = specsRepository.findAllByOrderFranchiseFranchiseOwnerFranchiseOwnerCode(franchiseCode);
        List<SpecsDTO> responseSpecs = new ArrayList<>();
        specsEntities.forEach(specs -> {
             responseSpecs.add(new SpecsDTO(specs));
        });

        return responseSpecs;
    }

    @Override
    public SpecsDTO getSpecsByFranchiseCode(int franchiseOwnerCode, int specsId) {

        Specs specs = specsRepository.findById(specsId).orElse(null);

        if (specs == null || specs.getOrder().getFranchise().getFranchiseOwner().getFranchiseOwnerCode() != franchiseOwnerCode){
            return null;
        }

        return new SpecsDTO(specs);
    }

}
