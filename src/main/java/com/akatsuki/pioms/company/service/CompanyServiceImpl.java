package com.akatsuki.pioms.company.service;

import com.akatsuki.pioms.company.dto.CompanyDTO;
import com.akatsuki.pioms.company.entity.Company;
import com.akatsuki.pioms.company.repository.CompanyRepository;
import com.akatsuki.pioms.company.vo.CompanyVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public CompanyVO findInformation() {
        return companyRepository.findById(1)
                .map(this::getCompanyDTO)
                .map(this::getCompanyVO)
                .orElse(null);
    }

    @Override
    public Company updateCompanyInfo(Company companyEntity) {
        return companyRepository.findById(1)
                .map(existingCompany -> {
                    Company updatedCompany = Company.builder()
                            .companyCode(existingCompany.getCompanyCode())
                            .companyName(companyEntity.getCompanyName())
                            .companyCall(companyEntity.getCompanyCall())
                            .companyEmail(companyEntity.getCompanyEmail())
                            .companyBusinessNum(companyEntity.getCompanyBusinessNum())
                            .companyAddress(companyEntity.getCompanyAddress())
                            .companyCeo(companyEntity.getCompanyCeo())
                            .companyFax(companyEntity.getCompanyFax())
                            .build();
                    return companyRepository.save(updatedCompany);
                })
                .orElseThrow(() -> new RuntimeException("ID로 회사를 못찾음: " + companyEntity.getCompanyCode()));
    }


    private CompanyDTO getCompanyDTO(Company companyEntity) {
        // Entity -> DTO
        CompanyDTO companyDTO = CompanyDTO.builder()
                .companyCode(companyEntity.getCompanyCode())
                .companyName(companyEntity.getCompanyName())
                .companyCall(companyEntity.getCompanyCall())
                .companyEmail(companyEntity.getCompanyEmail())
                .companyBusinessNum(companyEntity.getCompanyBusinessNum())
                .companyAddress(companyEntity.getCompanyAddress())
                .companyCeo(companyEntity.getCompanyCeo())
                .companyFax(companyEntity.getCompanyFax())
                .build();
        log.info("DTO Created: {}", companyDTO);
        return companyDTO;
    }

    private CompanyVO getCompanyVO(CompanyDTO companyDTO) {
        // DTO -> VO
        CompanyVO companyVO = CompanyVO.builder()
                .companyName(companyDTO.getCompanyName())
                .companyCall(companyDTO.getCompanyCall())
                .companyEmail(companyDTO.getCompanyEmail())
                .companyBusinessNum(companyDTO.getCompanyBusinessNum())
                .companyAddress(companyDTO.getCompanyAddress())
                .companyCeo(companyDTO.getCompanyCeo())
                .companyFax(companyDTO.getCompanyFax())
                .build();
        log.info("VO Created: {}", companyVO);
        return companyVO;
    }
}
