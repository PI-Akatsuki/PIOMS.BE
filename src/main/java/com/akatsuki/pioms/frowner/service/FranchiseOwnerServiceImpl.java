package com.akatsuki.pioms.frowner.service;

import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.aggregate.FranchiseOwnerVO;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FranchiseOwnerServiceImpl implements FranchiseOwnerService {

    private final FranchiseOwnerRepository franchiseOwnerRepository;

    @Autowired
    public FranchiseOwnerServiceImpl(FranchiseOwnerRepository franchiseOwnerRepository) {
        this.franchiseOwnerRepository = franchiseOwnerRepository;
    }

    // 전체 조회
    @Transactional(readOnly = true)
    @Override
    public List<FranchiseOwnerVO> findAllFranchiseOwners() {
        return franchiseOwnerRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .map(this::convertDTOToVO)
                .collect(Collectors.toList());
    }

    // 상세 조회
    @Transactional(readOnly = true)
    @Override
    public Optional<FranchiseOwnerVO> findFranchiseOwnerById(int franchiseOwnerCode) {
        return franchiseOwnerRepository.findById(franchiseOwnerCode)
                .map(this::convertEntityToDTO)
                .map(this::convertDTOToVO);
    }

    private FranchiseOwnerDTO convertEntityToDTO(FranchiseOwner franchiseOwner) {
        Franchise franchise = franchiseOwner.getFranchise();
        return FranchiseOwnerDTO.builder()
                .franchiseOwnerCode(franchiseOwner.getFranchiseOwnerCode())
                .franchiseOwnerName(franchiseOwner.getFranchiseOwnerName())
                .franchiseOwnerId(franchiseOwner.getFranchiseOwnerId())
                .franchiseOwnerPwd(franchiseOwner.getFranchiseOwnerPwd())
                .franchiseOwnerEmail(franchiseOwner.getFranchiseOwnerEmail())
                .franchiseOwnerPhone(franchiseOwner.getFranchiseOwnerPhone())
                .franchiseOwnerEnrollDate(franchiseOwner.getFranchiseOwnerEnrollDate())
                .franchiseOwnerUpdateDate(franchiseOwner.getFranchiseOwnerUpdateDate())
                .franchiseOwnerDeleteDate(franchiseOwner.getFranchiseOwnerDeleteDate())
                .franchiseName(franchise.getFranchiseName())
                .adminName(franchise.getAdmin().getAdminName())
                .build();
    }

    private FranchiseOwnerVO convertDTOToVO(FranchiseOwnerDTO dto) {
        return FranchiseOwnerVO.builder()
                .franchiseOwnerCode(dto.getFranchiseOwnerCode())
                .franchiseOwnerName(dto.getFranchiseOwnerName())
                .franchiseOwnerId(dto.getFranchiseOwnerId())
                .franchiseOwnerPwd(dto.getFranchiseOwnerPwd())
                .franchiseOwnerEmail(dto.getFranchiseOwnerEmail())
                .franchiseOwnerPhone(dto.getFranchiseOwnerPhone())
                .franchiseOwnerEnrollDate(dto.getFranchiseOwnerEnrollDate())
                .franchiseOwnerUpdateDate(dto.getFranchiseOwnerUpdateDate())
                .franchiseOwnerDeleteDate(dto.getFranchiseOwnerDeleteDate())
                .adminName(dto.getAdminName())
                .franchiseName(dto.getFranchiseName())
                .build();
    }
}
