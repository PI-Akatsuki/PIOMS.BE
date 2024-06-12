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
@RequestMapping("/admin")
@Tag(name = "[관리자]발주 API", description = "관리자 명세서 조회 관련 API")
public class AdminSpecsController {
    private final SpecsService specsService;
    @Autowired
    public AdminSpecsController(SpecsService specsService) {
        this.specsService = specsService;
    }

    @GetMapping("/specs")
    @Operation(summary = "전체 명세서 조회")
    public ResponseEntity<List<ResponseSpecs>> getSpecsList(@RequestParam int adminCode){
        List<SpecsDTO> specsDTOS = specsService.getSpecsListByAdminCode(adminCode);

        if (specsDTOS.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<ResponseSpecs> responseSpecs = new ArrayList<>();
        specsDTOS.forEach( specsDTO -> {
            responseSpecs.add(new ResponseSpecs(specsDTO));
        });
        return ResponseEntity.ok(responseSpecs);
    }

    @GetMapping("/specs/{specsCode}")
    @Operation(summary = "명세서 상세 조회")
    public ResponseEntity<ResponseSpecs> getSpecs(@RequestParam int adminCode,@PathVariable int specsCode){
        SpecsDTO specsDTO = specsService.getSpecsByAdminCode(adminCode,specsCode);
        return ResponseEntity.ok(new ResponseSpecs(specsDTO));
    }
}
