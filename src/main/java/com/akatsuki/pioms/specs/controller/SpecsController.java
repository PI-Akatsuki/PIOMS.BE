package com.akatsuki.pioms.specs.controller;


import com.akatsuki.pioms.specs.aggregate.ResponseSpecs;
import com.akatsuki.pioms.specs.service.SpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(specsService.getSpecsList());
    }
    @GetMapping("/admin/specs/{specsCode}")
    public ResponseEntity<ResponseSpecs> getSpecs(@PathVariable int specsCode){
        return ResponseEntity.ok(specsService.getSpecs(specsCode));
    }

    @GetMapping("/franchise/{franchiseCode}/specs")
    public ResponseEntity<List<ResponseSpecs>> getFranchiseSpecsList(@PathVariable int franchiseCode){return ResponseEntity.ok(specsService.getFranchiseSpecsList(franchiseCode));}

}
