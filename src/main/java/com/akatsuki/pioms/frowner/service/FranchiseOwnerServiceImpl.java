package com.akatsuki.pioms.frowner.service;

import com.akatsuki.pioms.frowner.aggregate.FranchiseOwner;
import com.akatsuki.pioms.frowner.dto.FranchiseOwnerDTO;
import com.akatsuki.pioms.frowner.repository.FranchiseOwnerRepository;
import com.akatsuki.pioms.franchise.aggregate.Franchise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public List<FranchiseOwnerDTO> findAllFranchiseOwners() {
        return franchiseOwnerRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    // 상세 조회
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<FranchiseOwnerDTO> findFranchiseOwnerById(int franchiseOwnerCode) {
        try {
            FranchiseOwner franchiseOwner = franchiseOwnerRepository.findById(franchiseOwnerCode)
                    .orElseThrow(() -> new RuntimeException("프랜차이즈 오너 코드를 찾을 수 없음: " + franchiseOwnerCode));
            FranchiseOwnerDTO dto = convertEntityToDTO(franchiseOwner);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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

}
