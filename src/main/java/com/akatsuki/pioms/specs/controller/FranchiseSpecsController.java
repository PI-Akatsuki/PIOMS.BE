package com.akatsuki.pioms.specs.controller;


import com.akatsuki.pioms.specs.aggregate.ResponseSpecs;
import com.akatsuki.pioms.specs.dto.SpecsDTO;
import com.akatsuki.pioms.specs.service.SpecsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/franchise")
@Tag(name = "[점주]발주 API", description = "점주 명세서 조회 관련 API")
public class FranchiseSpecsController {

    /**
     * <h1>명세서 컨트롤러</h1>
     * 점주: 자신의 발주 리스트를 조회할 수 있다.<br>
     *
     * */

    private final SpecsService specsService;

    @Autowired
    public FranchiseSpecsController(SpecsService specsService) {
        this.specsService = specsService;
    }


    @GetMapping("/specs/list")
    @Operation(summary = "점주의 명세서 전체 조회")
    public ResponseEntity<List<ResponseSpecs>> getFranchiseSpecsList(@RequestParam int franchiseOwnerCode){
        List<SpecsDTO> specsDTOS = specsService.getSpecsListByFrOwnerCode(franchiseOwnerCode);
        if (specsDTOS.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        List<ResponseSpecs> responseSpecs = new ArrayList<>();
        specsDTOS.forEach( specsDTO -> {
            responseSpecs.add(new ResponseSpecs(specsDTO));
        });
        return ResponseEntity.ok(responseSpecs);
    }

    @GetMapping("/specs/{specsId}")
    @Operation(summary = "점주의 명세서 상세 조회")
    public ResponseEntity<ResponseSpecs> getFranchiseSpecs(@RequestParam int franchiseOwnerCode, @PathVariable int specsId){
        SpecsDTO specsDTO = specsService.getSpecsByFranchiseCode(franchiseOwnerCode, specsId);

        if (specsDTO == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(new ResponseSpecs(specsDTO));
    }

}
