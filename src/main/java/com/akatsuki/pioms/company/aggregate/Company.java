package com.akatsuki.pioms.company.aggregate;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_code")
    private int companyCode;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_call")
    private String companyCall;

    @Column(name = "company_email")
    private String companyEmail;

    @Column(name = "company_business_num")
    private String companyBusinessNum;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "company_ceo")
    private String companyCeo;

    @Column(name = "company_fax")
    private String companyFax;

}
