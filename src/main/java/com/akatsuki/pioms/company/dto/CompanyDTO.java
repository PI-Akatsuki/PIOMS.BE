package com.akatsuki.pioms.company.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class CompanyDTO {
    private int companyCode;
    private String companyName;
    private String companyCall;
    private String companyEmail;
    private String companyBusinessNum;
    private String companyAddress;
    private String companyCeo;
    private String companyFax;

}

