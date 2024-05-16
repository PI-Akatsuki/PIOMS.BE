package com.akatsuki.pioms.specs.service;


import com.akatsuki.pioms.franchise.aggregate.DELIVERY_DATE;
import com.akatsuki.pioms.specs.aggregate.ResponseSpecs;
import com.akatsuki.pioms.specs.aggregate.SpecsEntity;
import com.akatsuki.pioms.specs.dto.SpecsDTO;

import java.util.List;

public interface SpecsService {

    List<SpecsDTO> getSpecsList();

    List<SpecsDTO> getFranchiseSpecsList(int franchiseCode);
    void afterAcceptOrder(int orderCode, int franchiseCode, DELIVERY_DATE deliveryDate);

    SpecsDTO getSpecsByFranchiseCode(int franchiseCode, int specsId);

    List<SpecsDTO> getSpecsListByAdminCode(int adminCode);

    SpecsDTO getSpecsByAdminCode(int adminCode, int specsCode);
}
