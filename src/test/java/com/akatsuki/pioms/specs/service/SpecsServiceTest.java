package com.akatsuki.pioms.specs.service;

import com.akatsuki.pioms.specs.aggregate.SpecsEntity;
import com.akatsuki.pioms.specs.dto.SpecsDTO;
import com.akatsuki.pioms.specs.repository.SpecsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SpecsServiceTest {

    SpecsRepository specsRepository;
    SpecsService specsService;
    @Autowired
    public SpecsServiceTest(SpecsRepository specsRepository, SpecsService specsService) {
        this.specsRepository = specsRepository;
        this.specsService = specsService;
    }

    @Test
    void getSpecsList() {
        List<SpecsEntity> specsList = specsRepository.findAll();
        List<SpecsDTO> specsDTOS = specsService.getSpecsList();
        assertEquals(specsList.size(),specsDTOS.size());
    }

    @Test
    void getFranchiseSpecsList() {
        int franchiseCode =1;
        List<SpecsEntity> specsList = specsRepository.findAllByOrderFranchiseFranchiseCode(franchiseCode);
        List<SpecsDTO> specsDTOS = specsService.getFranchiseSpecsList(franchiseCode);
        assertEquals(specsList.size(),specsDTOS.size());
    }

    @Test
    void afterAcceptOrder() {

    }

    @Test
    void getSpecsByFranchiseCode() {
        int franchiseCode = 1;
        int specsCode =1;
        SpecsEntity specs = specsRepository.findById(1).orElse(null);
        SpecsDTO specsDTO = specsService.getSpecsByFranchiseCode(franchiseCode,1);
        assertEquals(specs.getSpecsCode(),specsDTO.getSpecsCode());
    }

    @Test
    void getSpecsListByAdminCode() {
        List<SpecsEntity> specsList = specsRepository.findAll();
        List<SpecsDTO> specsDTOS = specsService.getSpecsList();
        assertEquals(specsList.size(),specsDTOS.size());
    }

    @Test
    void getSpecsByAdminCode() {
        List<SpecsEntity> specsList = specsRepository.findAll();
        List<SpecsDTO> specsDTOS = specsService.getSpecsList();
        assertEquals(specsList.size(),specsDTOS.size());
    }
}