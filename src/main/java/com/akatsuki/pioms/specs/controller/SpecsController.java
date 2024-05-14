package com.akatsuki.pioms.specs.controller;


import com.akatsuki.pioms.specs.aggregate.ResponseSpecs;
import com.akatsuki.pioms.specs.aggregate.SpecsEntity;
import com.akatsuki.pioms.specs.service.SpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SpecsController {

    /**
     * <h1>명세서 컨트롤러</h1>
     * 관리자: 모든 발주 리스트를 조회할 수 있다. <br>
     * 점주: 자신의 발주 리스트를 조회할 수 있다.<br>
     *
     * */

    private SpecsService specsService;

    @Autowired
    public SpecsController(SpecsService specsService) {
        this.specsService = specsService;
    }

    @GetMapping("/admin/specs")
    public ResponseEntity<List<ResponseSpecs>> getSpecsList(){
        List<SpecsEntity> specsDTOS = specsService.getSpecsList();
        List<ResponseSpecs> responseSpecs = new ArrayList<>();
        specsDTOS.forEach( specsDTO -> {
            responseSpecs.add(new ResponseSpecs(specsDTO));
        });
        return ResponseEntity.ok(responseSpecs);
    }
    @GetMapping("/admin/specs/{specsCode}")
    public ResponseEntity<ResponseSpecs> getSpecs(@PathVariable int specsCode){
        SpecsEntity specsDTO = specsService.getSpecs(specsCode);
        return ResponseEntity.ok(new ResponseSpecs(specsDTO));
    }

    @GetMapping("/franchise/{franchiseCode}/specs")
    public ResponseEntity<List<ResponseSpecs>> getFranchiseSpecsList(@PathVariable int franchiseCode){
        List<SpecsEntity> specsDTOS = specsService.getFranchiseSpecsList(franchiseCode);
        List<ResponseSpecs> responseSpecs = new ArrayList<>();
        specsDTOS.forEach( specsDTO -> {
            responseSpecs.add(new ResponseSpecs(specsDTO));
        });

        return ResponseEntity.ok(responseSpecs);
    }

}
