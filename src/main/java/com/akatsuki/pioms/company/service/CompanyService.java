package com.akatsuki.pioms.company.service;


import com.akatsuki.pioms.company.aggregate.Company;
import com.akatsuki.pioms.company.aggregate.CompanyVO;


public interface CompanyService {
    CompanyVO findInformation();

    Company updateCompanyInfo(Company companyEntity);
}
