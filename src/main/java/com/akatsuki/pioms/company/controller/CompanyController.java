package com.akatsuki.pioms.company.controller;

import com.akatsuki.pioms.company.service.CompanyService;
import com.akatsuki.pioms.company.vo.CompanyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "본사정보", description = "Company Information")
@RestController
@RequestMapping("/admin")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Operation(summary = "본사정보조회", description = "본사정보를 조회합니다.")
    @GetMapping("/information")
    public ResponseEntity<CompanyVO> getHeadquarters() {
        CompanyVO information = companyService.findInformation();
        return information != null ? ResponseEntity.ok(information) : ResponseEntity.notFound().build();
    }
}
