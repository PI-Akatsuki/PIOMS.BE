package com.akatsuki.pioms.company.service;


import com.akatsuki.pioms.company.entity.Company;
import com.akatsuki.pioms.company.vo.CompanyVO;


public interface CompanyService {
    CompanyVO findInformation();

    Company updateCompanyInfo(Company companyEntity);
}
