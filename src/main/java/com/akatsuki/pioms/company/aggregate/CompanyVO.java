package com.akatsuki.pioms.company.aggregate;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class CompanyVO {
    private String companyName;
    private String companyCall;
    private String companyEmail;
    private String companyBusinessNum;
    private String companyAddress;
    private String companyCeo;
    private String companyFax;

}
