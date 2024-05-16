package com.akatsuki.pioms.specs.controller;


import com.akatsuki.pioms.specs.aggregate.ResponseSpecs;
import com.akatsuki.pioms.specs.dto.SpecsDTO;
import com.akatsuki.pioms.specs.service.SpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/franchise")
public class FranchiseSpecsController {

    /**
     * <h1>명세서 컨트롤러</h1>
     * 관리자: 모든 발주 리스트를 조회할 수 있다. <br>
     * 점주: 자신의 발주 리스트를 조회할 수 있다.<br>
     *
     * */

    private SpecsService specsService;

    @Autowired
    public FranchiseSpecsController(SpecsService specsService) {
        this.specsService = specsService;
    }

    @GetMapping("/{franchiseCode}/specs/list")
    public ResponseEntity<List<ResponseSpecs>> getFranchiseSpecsList(@PathVariable int franchiseCode){
        List<SpecsDTO> specsDTOS = specsService.getFranchiseSpecsList(franchiseCode);
        List<ResponseSpecs> responseSpecs = new ArrayList<>();
        specsDTOS.forEach( specsDTO -> {
            responseSpecs.add(new ResponseSpecs(specsDTO));
        });
        return ResponseEntity.ok(responseSpecs);
    }
    @GetMapping("/{franchiseCode}/specs/{specsId}")
    public ResponseEntity<ResponseSpecs> getFranchiseSpecs(@PathVariable int franchiseCode, @PathVariable int specsId){
        SpecsDTO specsDTO = specsService.getSpecsByFranchiseCode(franchiseCode, specsId);
        if (specsDTO == null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        return ResponseEntity.ok(new ResponseSpecs(specsDTO));
    }

}
