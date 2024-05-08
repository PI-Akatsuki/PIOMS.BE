package com.akatsuki.pioms.specs.service;


import com.akatsuki.pioms.specs.controller.ResponseSpecs;

import java.util.List;

public interface SpecsService {

    List<ResponseSpecs> getSpecsList();

    ResponseSpecs getSpecs(int specsCode);

}
