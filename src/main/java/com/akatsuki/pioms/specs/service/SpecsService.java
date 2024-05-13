package com.akatsuki.pioms.specs.service;


import com.akatsuki.pioms.specs.aggregate.ResponseSpecs;
import com.akatsuki.pioms.specs.dto.SpecsDTO;

import java.util.List;

public interface SpecsService {

    List<SpecsDTO> getSpecsList();

    SpecsDTO getSpecs(int specsCode);

    List<SpecsDTO> getFranchiseSpecsList(int franchiseCode);

}
