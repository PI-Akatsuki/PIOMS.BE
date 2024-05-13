package com.akatsuki.pioms.specs.service;


import com.akatsuki.pioms.specs.aggregate.ResponseSpecs;
import com.akatsuki.pioms.specs.aggregate.SpecsEntity;

import java.util.List;

public interface SpecsService {

    List<SpecsEntity> getSpecsList();

    SpecsEntity getSpecs(int specsCode);

    List<SpecsEntity> getFranchiseSpecsList(int franchiseCode);

}
