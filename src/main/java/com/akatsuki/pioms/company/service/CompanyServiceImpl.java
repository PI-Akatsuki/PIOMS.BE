package com.akatsuki.pioms.company.service;

import com.akatsuki.pioms.company.dto.CompanyDTO;
import com.akatsuki.pioms.company.aggregate.Company;
import com.akatsuki.pioms.company.repository.CompanyRepository;
import com.akatsuki.pioms.company.aggregate.CompanyVO;
import com.akatsuki.pioms.log.etc.LogStatus;
import com.akatsuki.pioms.log.service.LogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class                             CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final LogService logService;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository,LogService logService) {
        this.companyRepository = companyRepository;
        this.logService = logService;
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
                    StringBuilder logMessage = new StringBuilder();
                    if (!existingCompany.getCompanyName().equals(companyEntity.getCompanyName())) {
                        logMessage.append(String.format("Name changed from %s to %s; ", existingCompany.getCompanyName(), companyEntity.getCompanyName()));
                    }
                    if (!existingCompany.getCompanyCall().equals(companyEntity.getCompanyCall())) {
                        logMessage.append(String.format("Call changed from %s to %s; ", existingCompany.getCompanyCall(), companyEntity.getCompanyCall()));
                    }
                    if (!existingCompany.getCompanyEmail().equals(companyEntity.getCompanyEmail())) {
                        logMessage.append(String.format("Email changed from %s to %s; ", existingCompany.getCompanyEmail(), companyEntity.getCompanyEmail()));
                    }
                    if (!existingCompany.getCompanyBusinessNum().equals(companyEntity.getCompanyBusinessNum())) {
                        logMessage.append(String.format("BusinessNum changed from %s to %s; ", existingCompany.getCompanyBusinessNum(), companyEntity.getCompanyBusinessNum()));
                    }
                    if (!existingCompany.getCompanyAddress().equals(companyEntity.getCompanyAddress())) {
                        logMessage.append(String.format("Address changed from %s to %s; ", existingCompany.getCompanyAddress(), companyEntity.getCompanyAddress()));
                    }
                    if (!existingCompany.getCompanyCeo().equals(companyEntity.getCompanyCeo())) {
                        logMessage.append(String.format("CEO changed from %s to %s; ", existingCompany.getCompanyCeo(), companyEntity.getCompanyCeo()));
                    }
                    if (!existingCompany.getCompanyFax().equals(companyEntity.getCompanyFax())) {
                        logMessage.append(String.format("Fax changed from %s to %s; ", existingCompany.getCompanyFax(), companyEntity.getCompanyFax()));
                    }
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
                     companyRepository.save(updatedCompany);

                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    String username = authentication.getName();
                    if (logMessage.length() > 0) {
                        logService.saveLog(username, LogStatus.수정, "통합관리자 로그인", "Company");
                    }

                    return updatedCompany;
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
